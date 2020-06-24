# map-tile-service

Spring-boot service for storing large raw images and serving compressed tiles or sub-sections of the image according to the given parameters.

## Usage
It is possible to run this example on a machine with 4GB ram, however 8GB or greater if you're multitasking is more comfortable.
You may notice it takes a little longer than most projects to check out - this is because the sample images are large. About 1GB is required to store the git history and build the project.

There is an example frontend here: https://github.com/adrianjoconnor/image-tile-fe

To checkout both, run the following command (requires maven):

`git clone https://github.com/adrianjoconnor/map-tile-service.git && git clone https://github.com/adrianjoconnor/image-tile-fe.git && cd map-tile-service && mvn clean install`

If maven completes successfully (Showing a "BUILD SUCCESS" message), then you can proceed to run the backend service:

`mvn spring-boot:run`

If you don't want to run the example frontend, you can stop here.

In a separate terminal, cd to the directory containing your copy of image-tile-fe:

`cd image-tile-fe`

run npm install (requires npm)

`npm install`

And then run this:
 
`npm run serve`

Then open your browser and go to http://localhost:8081 and it should all be running.

If you'd prefer, you can build a docker image for the backend by running 
`docker build -t adrianoc/map-tile-service .`
from the root directory of the project.
