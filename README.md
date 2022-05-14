# Spotify Social Media Network

## Running this project
* Before you start the project, ensure that the files secret/client_id.txt and secret/client_secret.txt 
are present. These must contain the client id and secret given in the Spotify dashboard. 
* You must also ensure that any account you intend to log into the site has been added in the 
Spotify dashboard. 
* Add any callback url you intend to use in the Spotify dashboard.
* Configure the addresses in [site/.env](site/.env) and [server/src/main/java/edu/brown/cs/student/main/Main.java](server/src/main/java/edu/brown/cs/student/main/Main.java)
* The dynamic server is started by running Main.java and passing in as args the paths to 
* secret/client_id.txt, secret/client_secret.txt, and secret/known.sqlite3.
* The static server must be started by running `npm start` in the [site](site) directory.

## Testing
* There are unit tests in the dynamic server that may be run with `mvn package`.
* There is also a selenium bot that functions to provide front end testing. This bot can be
enabled through the normal unit test interface. 
* In order to run the api tests, you must load the file secret/access_token.txt with a valid access token.

## Specifications
The specification document can be found [here](https://docs.google.com/document/d/1RpRd8VrG_hTVj1xbJnsyPMMRMbHINGpbKk06meK5d1Q/edit?usp=sharing)