FROM ubuntu:latest
LABEL authors="os1"

ENTRYPOINT ["top", "-b"]