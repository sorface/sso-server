FROM redis

COPY docker/redis/redis.conf /usr/local/etc/redis/

ENTRYPOINT [ "redis-server", "/usr/local/etc/redis/redis.conf" ]