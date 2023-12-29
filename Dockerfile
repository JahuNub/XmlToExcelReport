FROM ubuntu:latest
LABEL authors="matme"

ENTRYPOINT ["top", "-b"]