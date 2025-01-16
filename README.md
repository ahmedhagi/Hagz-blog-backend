# Hagz Blogging Back-end Application

https://hagz-blog.vercel.app/

## Description
This is the back-end side of the Hagz Blog website. 

This application allows the user to view posts created by other users on a variety of different topics and tags. Users are able to create, edit, and delete posts by signing up and logging into their account. The application allows other users to comment on posts to engage with the community. Please refer to the usage section for a more detailed explanation of the usages of the application. 

This application is built using the Spring Boot framework and utilizing the Spring Data JPA and Hibernate libraries. The application is built and deployed by using the Docker software platform.

## E-R Diagram for the application

![Hagz-Blog_Er_Diagram](https://github.com/user-attachments/assets/690020d4-86f8-4119-9b7d-1a814b2acd1a)


## Installation & Run

In application.properities file switch the spring.profiles.active to dev by uncommeting "spring.profiles.active=dev" and commeting "spring.profiles.active=prod"

```
#######################################################
# Spring Profiles Configuration
#######################################################

# Active profile for development
 spring.profiles.active=dev

# Active profile for production
# Uncomment the following line for the production environment
# spring.profiles.active=prod

#######################################################
# End of Configuration
#######################################################
```

You should alter the database configuration in the application-dev.properties file before launching the API server.
Change the username, password, and port number in accordance with your local database configuration. Also change the JWT_SECERT
to your prefered configuration.

```
spring.datasource.url=jdbc:mysql://localhost:3306/${DB_DATABASE}?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
hagz.app.jwtSecret= ${JWT_SECRET}
```

Access the complete fullstack application at https://hagz-blog.vercel.app/

## API Root Endpoint

https://localhost:8000/

https://localhost:8000/api/

## API Request Bodies 

Specific endpoints require Request Bodies

### SignupRequest:
```
{
    "username":"valid and unique string",
    "email":"valid email string",
    "password":"valid string",
    "role": ["names of roles ex. "user"]
}
```

### SignInRequest:
```
{
    "username" : "registered username",
    "password" : "password associated with registered user"
}
```

### PostRequest:

* For the topicName field it's best to run the 'GET api/topics/all' endpoint to find a valid topic name
* Similarally For the tagSet field it's best to run the 'GET api/topics/all/tags/:topicName' endpoint to find a valid tag names

```
{
    "title" : "valid string",
    "content" : "valid string",
    "topicName": "vaild topic name",
    "shortDesc" : "valid string",
    "imageUrl" : "valid string",
    "tagSet": [ subset of valid tag names]
    
}
```

### CommentRequest:

```
{
	content : "valid string"
}
```

### TopicRequest:

```
{
	name : "valid string"
}
```

### TagRequest: 

```
{
	
	tags:["list of valid tag names"]
}
```

### UserRequest: 

```
{
	"username":"register username",
	"bio":"valid string"
    "imageURL":"valid string"
}
```


## API Endpoints

Some requests require authorization in the form of Bearer token. This token can be obtained by envoking the 'POST /api/auth/signup' endpoint from the response body.

### Auth
* `POST /api/auth/signin`: Register a new user (SignUpRequest required)
* `POST /api/auth/signup`: Logs in user (LoginRequest required)


### Post
* `GET /api/posts/all`: Gets all posts
* `GET /api/posts/:id`: Gets post by post id
* `GET /api/posts//api/posts/get/pagination/:offset/:pageSize/`: Gets page of all posts based off the offset and pageSize
* `GET /api/posts//api/posts/get/username/:offset/:pageSize/:username`: Gets page of all posts based off the username, offset and pageSize
* `GET /api/posts//api/posts/get/topic/:offset/:pageSize/:topic`: Gets page of all posts based off the topic name, offset and pageSize
* `GET /api/posts//api/posts/get/tag/:offset/:pageSize/:tag`: Gets page of all posts based off the tag name, offset and pageSize
* `POST /api/posts/new_post`: Creates new post (PostRequest required) (Authorization Bearer token required)
* `PUT /api/posts/update/:id`: Updates post based on a given post id (fields from PostRequest required) (Authorization Bearer token required)
* `DELETE /api/posts/delete/:id`: Deletes post base on a given post id (Authorization Bearer token required)


#### Comment
* `GET /api/comment/get/:id`: Gets comment based on the comment id
* `POST /api/comment/:id/comment`: Adds comment to post given post id (CommentRequest required) (Authorization Bearer token required)
* `PUT /api/comment/:id/update`: Updates comment based on the comment id (Authorization Bearer token required)
* `DELETE /api/comment/:id/delete`:  Deletes comment based on the comment id (Authorization Bearer token required)

#### Topic
* `GET api/topics/all`: Gets all topics
* `GET api/topics/all/tags/:topic`: Gets all tags based on the topic name
* `POST /api/topics/create`: Create a new topic (TopicRequest required) (Authorization Bearer token required)
* `POST /api/topics/add/:topic`: Adds tags to topic based on the topic name (TagRequest required) (Authorization Bearer token required)

#### Tag
* `POST /api/tags/`: Create new tags ('TagRequest required') (Authorization Bearer token required)

#### User
* `GET /api/user/get/:username`: Gets user info given a username
* `PUT /api/user/update`: Updates user info ('UserRequest required') (Authorization Bearer token required) 

## Contributing

Pull requests are welcome. For major changes, please open an issue first. 
To discuss what you would like to change.

## License

Distributed under the Unlicense License. See LICENSE.txt for more information. 
