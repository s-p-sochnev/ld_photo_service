## LD photo service task
### Overview
REST application providing basic interface to an FTP server.
Application scans remote server collecting a list of photos falling under certain conditions.
It can provide a list of those files with some additional information. 
Also, it can display a picture when its path is specified.  

### How to run   
In order to run the application, specify FTP address and credentials.
Host address and port are provided. 
Username (*ftp.username*) and password (*ftp.password*) should be set manually in application.properties file 
or via command line when starting application: 
> java -jar ld_photo_service-1.0.jar --ftp.username=user --ftp.password=123  

By default application listens on port 8087.

### Endpoints
- GET **/photos**  
Returns a list of DTOs containing information about available pictures matching predefined conditions:
  - Located in a directory with name provided in *photos.folder.name* property
  - File name matches a regular expression in *photos.file.mask* property  

  Each DTO contains following information about the picture:
  - Full path to the file on server
  - Timestamp
  - File size in bytes


- GET **/photo**  
Expects parameter '**path**' - full path to the file on server.
Returns photo as a picture.


### Technology stack
- Java 17
- Spring (Boot, MVC)
- Spring Integration FTP
- Maven
