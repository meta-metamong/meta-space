FROM node:22.13.1-alpine
RUN ["mkdir", "/src"]
WORKDIR /src
COPY . ./
RUN ["npm", "install"]
ENTRYPOINT ["npm", "run", "build"]