docker build -t report .
docker stop report || true
docker rm report || true
docker run -d -p 8080:8080 --name report report