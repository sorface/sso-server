FROM node:16-alpine3.16 as sso-front

WORKDIR /source

ARG REACT_APP_BACKEND_URL
ARG REACT_APP_WS_URL
ARG REACT_APP_VERSION

ENV REACT_APP_BACKEND_URL ${REACT_APP_BACKEND_URL}
ENV REACT_APP_WS_URL ${REACT_APP_WS_URL}
ENV REACT_APP_VERSION ${REACT_APP_VERSION}

COPY client/ /source

RUN npm ci

RUN npm run build

FROM nginx:1.23.1-alpine

ARG NGINX_SECURITY_BACKEND_URL
ARG NGINX_DOMAIN

ENV NGINX_SECURITY_BACKEND_URL ${NGINX_SECURITY_BACKEND_URL}
ENV NGINX_DOMAIN ${NGINX_DOMAIN}

WORKDIR /usr/share/nginx/html

COPY docker/proxy/nginx.conf /etc/nginx/templates/nginx.conf.template
COPY --from=sso-front /source/build/  /home/html/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]