## **Overview**
Simple Note Application with RESTful Api developed as backend developer assignement task.
Technologies:

 - JAVA 11
 - Spring (Boot, MVC, Data, Validation)
 - JPA (Hibernate)
 - MySQL Database
 ## Features

 **CREATE NOTE**
 Suppose the application runs at localhost.
 To create note send HTTP POST Request to:
http://localhost:8080/notes

Request should have the following JSON Body:

    {
    	"title":  "example title",
    	"content":  "example content"
    }

The title and the content must not be empty! Otherwise, server responses with _HTTP Status_ Code 422( Unprocessable Entity).

If the Note is succesfully created server responses with HTTP Status Code 201 (Created) and JSON with newly created Note id:

    {
    	"id": 1,
    	"title":  "example title",
    	"content":  "example content"
    }

If you tried to create Note that exist in application (Note with existing title) server would response with HTTP Status Code 409 (Conflict).

**READ NOTE**
There are two ways to get the note.
1.  Send HTTP GET Request to:
http://localhost:8080/notes/{id} where id is the id of note you want

2. Send HTTP GET Request to:
http://localhost:8080/notes/title?title=exampletitle with title of note as param

Server would response with HTTP Status Code 200 and the following JSON:

    {
    "id":  1,
    "title":  "note title",
    "content":  "note content",
    "created":  "2020-10-04T19:18:08",
    "modified":  "2020-10-04T19:18:08"
    }

Created is the timestamp of saving the first note version.
Modified is the timestamp of saving the most recent note version.
All versions of each note are available in Archive Service.


If the note does not exist server would response with HTTP Status Code 204 (No Content).

To get list of all notes send GET REQUEST to:
http://localhost:8080/notes/getall

**UPDATE NOTE** 
To update note send HTTP PUT request to:

http://localhost:8080/notes

containing JSON with title of updating note and new content: 

    {
    	"title":  "title of note which you want update",
    	"content":  "new note content"
    }

Title and content must not be empty, and if the note you want to update does not exist server responses with HTTP Status Code 204 (No Content)
If given note is succesfully updated server responses with Status Code 200 (OK).

**DELETE NOTE**
To delete note send HTTP DELETE REQUEST  to:
http://localhost:8080/notes/{id} where id is the id of note that you want to delete. 

Or to:
http://localhost:8080/notes/title?title=exampletitle with title of note as a param.

Server responses with Status 200 for successful operation or 204 when given note does not exist.

WARNING!
Deleted Note and all of its previous versions are available in Archive Service.

## Archive Service
There is a service for getting all versions of all notes.
To get list of all notes contains every version of each note send HTTP GET Request to:
http://localhost:8080/archive/getall

Server will response with JSON. For example:

    [
	    {
		    "id":  1,
		    "title":  "first note",
		    "versions":  [
				  { 
			    "content":  "first version",   
			    "date":  "2020-10-06T19:50:53"   
				  }  
						    ] 
	    },  
	    { 
		    "id":  2,   
		    "title":  "second note",  
		    "versions":  [    
			    {   
			    "content":  "first version",    
			    "date":  "2020-10-06T19:51:13"    
			    },    
			    {    
			    "content":  "second version",    
			    "date":  "2020-10-06T19:51:28"    
			    },    
			    {    
			    "content":  "third version",    
			    "date":  "2020-10-06T19:51:36"    
			    }    
				    ]   
    }    
    ]

## How to run application
Pull repository and open it in any JAVA IDE(Preferred InteliJ IDEA). You have to have JDK 11 or higher and Maven installed.

Spring Boot has embedded server (Tomcat - servelt container) so you do not need any other. 

Application needs MySQL database. Create an empty database on your MySql server(You can use for example MySQL WorkBench).
Then in notes-app/src/main/resources write in application.properties file the following setup:

> spring.datasource.url=jdbc:mysql://localhost:3306/*yourDataBaseName*?useSSL=false&serverTimezone=UTC
> spring.datasource.username=*database_username* 
> spring.datasource.password=*database_password* 
> spring.jpa.hibernate.ddl-auto=create-drop  
> server.error.include-message=always  
> spring.datasource.tomcat.initial-size=3

Hibernate will automatically create all necessary tables as it is described in note-app code.

You can run application from IDE or from console.

To test project I recomend POSTMAN:

https://www.postman.com/downloads/

It is far better and more practical than typing curl commands

## Documentation
Project has documentation in JavaDocs. You can generate it as html page by typing in console:
mvn javadoc:javadoc

Then open notes-app/target/site/apidocs/index.html



