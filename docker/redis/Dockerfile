FROM redis/redis-stack:latest

WORKDIR /usr/local/redis/

ENV PORT 6379
EXPOSE 6379

CMD ["redis-stack-server --requirepass '$${REDIS_PASSWORD:?REDIS_PASSWORD variable is not set}'"]