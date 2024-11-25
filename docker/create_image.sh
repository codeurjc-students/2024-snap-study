#sets build context to parent and builds using Dockerfile from docker directory
docker build -t jrodriguezs2020/snapstudy -f docker/Dockerfile .
#Pushes the image to dockerhub
docker push jrodriguezs2020/snapstudy
#Run the image using docker-compose from docker directory
docker-compose up