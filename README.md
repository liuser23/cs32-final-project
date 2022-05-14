# Spotify Social Media Network

## Running this project
* Before you start the project, ensure that the files secret/client_id.txt and secret/client_secret.txt 
are present. These must contain the client id and secret given in the Spotify dashboard. 
* You must also ensure that any account you intend to log into the site has been added in the 
Spotify dashboard. 
* Add any callback url you intend to use in the Spotify dashboard.
* Configure the addresses in [site/.env](site/.env) and [server/src/main/java/edu/brown/cs/student/main/Main.java](server/src/main/java/edu/brown/cs/student/main/Main.java)
* The dynamic server must be started by running Main.java,
and the static server must be started by running `npm start` in the [site](site) directory.

## Testing
* There are unit tests in the dynamic server that may be run with `mvn package`.
* Testing in the front end is manual: you must test features individually and see that you are 
satisfied with the operation of the site. 

