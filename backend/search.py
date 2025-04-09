# search.py
import sys
import json
from opensearchpy import OpenSearch, RequestsHttpConnection
from requests_aws4auth import AWS4Auth
import boto3

def opensearch_multi_match_query(query, db_index):
    region = 'eu-west-1'
    service = 'es'
    host = 'search-snapstudy-opensearch-fjhm6pvyao62q5pw5jqonbvthy.eu-west-1.es.amazonaws.com'
    index_name = db_index

    session = boto3.Session()
    credentials = session.get_credentials().get_frozen_credentials()

    awsauth = AWS4Auth(
        credentials.access_key,
        credentials.secret_key,
        region,
        service,
        session_token=credentials.token
    )

    client = OpenSearch(
        hosts=[{'host': host, 'port': 443}],
        http_auth=awsauth,
        use_ssl=True,
        verify_certs=True,
        connection_class=RequestsHttpConnection
    )

    body = {
        "query": {
            "multi_match": {
                "query": query,
                "fields": ["title^2", "content^1"],
                "fuzziness": "AUTO"
            }
        }
    }

    try:
        response = client.search(index=index_name, body=body)
        results = []
        
        for hit in response['hits']['hits']:
            results.append({
                "index": hit['_source'].get('index', ''),  # Assuming 'index' is a field in the source
                "title": hit['_source'].get('title', '')  # Title field from the source
            })

        output = {
            "query": query,
            "total_results": response['hits']['total']['value'],
            "results": results
        }

        print(json.dumps(output))

    except Exception as e:
        error_response = {"error": str(e)}
        print(json.dumps(error_response))
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(json.dumps({"error": "Faltan argumentos: query, index_name"}))
        sys.exit(1)

    query = sys.argv[1]
    index_name = sys.argv[2]

    opensearch_multi_match_query(query, index_name)