FROM redis/redis-stack:latest

WORKDIR /usr/local/redis/

RUN echo ${REDIS_PASSWORD}

ENV PORT 6379
EXPOSE 6379

CMD [ "redis-stack-server", "--requirepass", "${REDIS_PASSWORD}"]