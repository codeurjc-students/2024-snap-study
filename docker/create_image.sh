#sets build context to parent and builds using Dockerfile from docker directory
docker build -t jrodriguezs2020/snapstudy2 -f docker/Dockerfile .
#Pushes the image to dockerhub
docker push jrodriguezs2020/snapstudy2
#Run the image using docker-compose from docker directory
docker-compose up