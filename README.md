## Email Listening System

This system listens for incoming emails using Google's Pub/Sub service.
It consists of two main components: Authentication and Email Listening.

### System Overview

**1-Authentication:**

This step is used to obtain a token from Google services. 
The process begins at the endpoint `http://localhost/login`.
To start the authentication process and obtain a token, you'll need the following:

Client ID
Redirect URL
Required scopes

**2-Email Listening:** 

After authentication, the system starts listening to a topic in Google's Pub/Sub system based on your configuration in Google Cloud.

## Technology Used
 Spring Boot
- **Version**: 4.0.0

Java
- **Version**: 21

Build Tool
 - **Maven**:

  
**Spring Boot Web Starter**: 
  - Provides the necessary components to build web applications, including RESTful services.

**Google API Client**: 
  - Facilitates communication with Google APIs, enabling the application to interact with various Google services.

**Google Gmail API**: 
  - Provides access to Gmail features, allowing the application to send, read, and manage Gmail emails.

**Spring WebFlux**: 
  - Supports building reactive web applications using the reactive programming model.

**Google OAuth Client Jetty**: 
  - Handles OAuth authentication, particularly with Jetty, to secure communication with Google services.

**Google HTTP Client Jackson2**: 
  - Supports JSON parsing and serialization in HTTP requests and responses.

## Clone the Project

To clone the project to your local machine, use the following Git command:

```bash
git clone https://github.com/mustafa-abuelmagd/email-listener.git
```


## Setup Instructions


### Implement these in your Google Cloud project settings:

### 1- Go to the Google Cloud Platform Console:
`https://console.cloud.google.com/apis/`

### 2-Create a new project:

**Click on the project dropdown near the top of the page.**

**Select "New Project" or use the search bar to find "New project".**

**Create a new project (write project name > click create).**

**Activate your project in the list of projects.**

![Screenshot from 2024-09-03 19-01-48](https://github.com/user-attachments/assets/ad599b87-ef05-4323-a7eb-ecfd29fb3498)


![Screenshot from 2024-09-03 19-07-40](https://github.com/user-attachments/assets/8a1b7ac9-c9e2-4530-82b5-12aaec4f423e)

![Screenshot from 2024-09-07 12-39-40](https://github.com/user-attachments/assets/99b852d9-79c8-4d20-91cc-f5cf6dfdbd21)



### 3- Enable the Gmail API:

**Go to "APIs & Services" > "Library".**

**Search for "Gmail API" and select it**

**Click "Enable".**

![image](https://github.com/user-attachments/assets/2f1015f0-b293-42dc-be0f-e4add7016c2f)

![Screenshot from 2024-09-07 12-46-54](https://github.com/user-attachments/assets/ccda0e2a-a67a-4e6e-8d43-5b8b9023dbab)


![Screenshot from 2024-09-03 19-12-47](https://github.com/user-attachments/assets/09ca39a1-1057-4e02-a68a-013b1ea31882)

![Screenshot from 2024-09-07 12-48-26](https://github.com/user-attachments/assets/0f7c4112-4a18-40db-85b1-19c557953b3e)


### 4-Configure OAuth credentials:

**In "APIs & Services", go to "Credentials".**

**Click "Create Credentials" > "OAuth client ID".**
**In "OAuth client ID" >  `CONFIGURE CONSENT SCREEN` .**
**In OAuth Consent screen > `External`  and then `CREATE`.**

**Choose "Web application" as the application type.**

**Choose "User data"**



![Screenshot from 2024-09-07 12-51-15](https://github.com/user-attachments/assets/00cb5ebc-415e-45e2-a2e6-1afa7b5fd562)

![Screenshot from 2024-09-07 12-52-31](https://github.com/user-attachments/assets/35b076ff-027b-4447-88ef-1ee43c3539ab)

![Screenshot from 2024-09-07 12-56-23](https://github.com/user-attachments/assets/ff80676f-f9c8-4680-a756-bfa75bdab9f6)



![Screenshot from 2024-09-07 12-57-36](https://github.com/user-attachments/assets/da60a810-a833-4cbd-bf41-6952d54fa26c)




### 5- OAuth Consent Screen

**1- APP information :**
The name of the app asking for consent

**2- User support email :**
The User Support Email field requires you to specify an email address that will be displayed to users on the consent   screen.  This should be an email address that you regularly monitor so that you can answer questions they have about sign-in

**3-  Developer contact information**
These email addresses are for Google to notify you about any changes to your project.

and click `SAVE AND CONTINUE`

  ![Screenshot from 2024-09-07 14-51-05](https://github.com/user-attachments/assets/aee6a678-5207-47b4-b4a4-01e49ab78fca)

![Screenshot from 2024-09-07 14-51-59](https://github.com/user-attachments/assets/26acf420-05b0-49d3-bd93-95f201561902)


![Screenshot from 2024-09-07 15-01-23](https://github.com/user-attachments/assets/ff8a6af2-b656-44d6-ba44-de59ecd09262)



### 7-Scopes

**In the OAuth consent screen, click `Add or Remove Scopes`.

**Add the following scopes:**
 ```pach
  https://mail.google.com/
 https://www.googleapis.com/auth/gmail.readonly
```
![Screenshot from 2024-09-03 20-13-51](https://github.com/user-attachments/assets/08625df8-be93-4133-930d-46b4313358bc)

![Screenshot from 2024-09-07 15-06-01](https://github.com/user-attachments/assets/a8372488-96d7-4c9c-be83-b04a459f0229)
 
 **CLICK >  `ADD TO TABLE` .**

![Screenshot from 2024-09-03 20-28-00](https://github.com/user-attachments/assets/5266fbb2-1269-4ca3-be2d-1e8cb5072b9d)

**After finished click click `SAVE AND CONTINUE`**


###  8-Test users
While publishing status is set to "Testing", only test users are able to access the app. Allowed user cap prior to app verification is 100

**1-Click `ADD USERS`**

**2-Add users**

![Screenshot from 2024-09-07 15-13-52](https://github.com/user-attachments/assets/331ef73c-9ecb-48b6-98fd-212af661b9a4)

![Screenshot from 2024-09-07 15-15-32](https://github.com/user-attachments/assets/d735e4bd-7ea8-4f7d-8416-bbb1c052f215)

![Screenshot from 2024-09-07 15-16-53](https://github.com/user-attachments/assets/bdd65619-663a-4ea1-beeb-f6c88dcf322a)

**After finished click click `SAVE AND CONTINUE`**

![Screenshot from 2024-09-07 15-20-05](https://github.com/user-attachments/assets/ab053db7-c313-48e0-8439-814637fb93c1)
 
 click `BACK TO DASHBOARD`

## ------------------------------------
 ## Create Credential to Gmail API 
 
1- go to library
2- search for Gmail api 
3 select Manage
4- choose `CREATE CREDENTIALS`

![image](https://github.com/user-attachments/assets/e251c560-27a7-49f8-80e6-a8081705734e)

![Screenshot from 2024-09-07 17-42-14](https://github.com/user-attachments/assets/9e2d93a3-7d79-4985-8a41-e66e30149a68)

![Screenshot from 2024-09-07 17-42-54](https://github.com/user-attachments/assets/b5ac6d7d-57ce-44a7-bc00-f1ed3c7e52e0)

![Screenshot from 2024-09-07 17-45-16](https://github.com/user-attachments/assets/10123f57-7c78-4295-ae27-fb0f27f3346e)


### 2- Credential Type
  **choose `User data` and click `NEXT`**
  ![Screenshot from 2024-09-07 17-48-33](https://github.com/user-attachments/assets/485b8820-b119-4acd-939e-93483202da94)

### 3-add the scope 
click next direct because you implemented it in a previous step


### 4- OAuth Client ID
![Screenshot from 2024-09-04 08-02-49](https://github.com/user-attachments/assets/caef98fa-c0e6-4bbb-8771-9b71922806c5)


**Authorized JavaScript origins :** ``` http://localhost:8080 ```

**Authorized redirect URIs :**  ``` http://localhost:8080/redirect ```

![Screenshot from 2024-09-04 08-31-32](https://github.com/user-attachments/assets/f40dfdc8-2e6d-4336-8b29-d8db56aff19b)

 **and click `NEXT`**

### We now have the client ID




## Create TOPIC
 
**Search in a tab by keyword "Topic" and choose the  topic.**

![Screenshot from 2024-09-07 15-25-37](https://github.com/user-attachments/assets/7d1331c5-f40f-4c6b-b50d-0b7e597bdf99)

**write name topic and click `CREATE`**



![Screenshot from 2024-09-07 15-26-30](https://github.com/user-attachments/assets/4e65fe57-f10a-46ae-8297-69c9c4b74538)

 
## Subscriptions

**1- go to Subscription page**

**2-choose Subscription ID and click `EDIT`**

**5- Under "Delivery Type", select "Push".**

**6- For the Endpoint URL, you'll need a public URL that can receive messages from Pub/Sub. We'll use ngrok for this**



![Screenshot from 2024-09-07 18-01-10](https://github.com/user-attachments/assets/5435670c-e6f0-4f6b-8578-2088a5300b96)


![Screenshot from 2024-09-07 18-06-05](https://github.com/user-attachments/assets/373837fd-39af-4fe3-bda0-7a1f3cc11fa1)

![Screenshot from 2024-09-07 18-07-40](https://github.com/user-attachments/assets/35647505-f596-4406-992b-27cc6fbf7f8c)

![Screenshot from 2024-09-07 18-13-14](https://github.com/user-attachments/assets/92b943e1-907a-4e68-ac55-14ec0f08fc01)

**Create a public URL using Ngrok.**

![Screenshot from 2024-09-04 10-23-11](https://github.com/user-attachments/assets/39b79519-6d2d-4963-8a57-d53856d093fd)



![Screenshot from 2024-09-04 10-22-23](https://github.com/user-attachments/assets/eba6c7b7-df9b-4947-83fc-7caaf2f46601)

**add ngrok url + `/receive`**

![Screenshot from 2024-09-04 10-21-31](https://github.com/user-attachments/assets/3abb25ef-3a44-4a6b-a036-ae36d0450e98)
 

**in below click `UPDATE`**




## 2. Application Configuration

After setting up Google Cloud, configure your application:

**Copy your `Client ID` , `Secret ID` , `Topic Name` from the Google Cloud Console to your project **

( CREDENTIALS > `web client 1` >  information and summary  )**

  ![Screenshot from 2024-09-03 20-35-07](https://github.com/user-attachments/assets/50293595-3fe9-443f-bf4d-36507255602f)

( Subscription >  Subscription ID > you will see `Topic name` )
![image](https://github.com/user-attachments/assets/1f1d413f-b15a-4d4a-b59a-a47e024780df)

 ** add [ `Topic name` , `Client ID` , `Client secret` ,  `project name in cloud` ] to the `application.yml` file in the code.**

### Usage

**Start the application .**

**Open your web browser and navigate to**

```
http://localhost:8080/login
```
#### Follow these steps to complete the authentication process:

**a. You will be redirected to the Google sign-in page.**

**b. Select the Google account you want to use with this application.**

**c. Review the permissions requested by the application and click "Allow".**

**d. After granting permissions, you will be redirected back to your application.**

Upon successful authentication, you will receive an access token.

**This token is required for the application to access your Gmail account and listen for incoming emails.
The application will automatically store and use this token for subsequent operations.**

## Listening 
**Once authenticated, the system will begin listening for emails using the configured Pub/Sub topic.**

**When anyone sends an email to your authenticated Gmail account, you will see it in your project.**
If you're not seeing incoming emails in your project:

#### Ensure that your application is still running and connected to the Pub/Sub topic.

**1-** Check that the Gmail account you authenticated with is the one receiving the emails.

**2-** Verify that your ngrok tunnel is still active and that the Pub/Sub subscription has the correct endpoint URL.

**3-** Review the application logs for any error messages or connection issues.

 ## How to Start the App
 
**Before starting the application, ensure that you have completed the **Setup Instructions**. There are two ways to start the application:**

### 1. Using IntelliJ

1. Open the project in IntelliJ IDEA.
   
2. Make sure all necessary dependencies are downloaded and the project is built successfully.
  
3. Right-click on the `EmailListenerApplication` class and select **Run 'Application'**.

4-Open your web browser or a tool like Postman.

5-Access the authentication endpoint by navigating to:
```
http://localhost:8080/login
```

6- the output 

![Screenshot from 2024-09-04 12-43-26](https://github.com/user-attachments/assets/19cf0a7a-f229-4671-bb29-46dbff64a2bf)


## Resources Used to Build This Project

https://blog.postman.com/how-to-access-google-apis-using-oauth-in-postman/

https://stackoverflow.com/questions/11330919/correct-redirect-uri-for-google-api-and-oauth-2-0

https://developers.google.com/identity/protocols/oauth2/web-server

https://developers.google.com/identity/protocols/oauth2/javascript-implicit-flow

https://infisical.com/blog/guide-to-implementing-oauth2

https://forum.bubble.io/t/how-do-i-implement-an-oauth2-flow-in-my-ui/17113

https://forum.bubble.io/t/how-do-i-implement-an-oauth2-flow-in-my-ui/17113

https://developers.google.com/identity/protocols/oauth2

https://support.google.com/cloud/answer/6158849?hl=en

https://blog.logrocket.com/how-to-authenticate-access-google-apis-using-oauth-2-0/

https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8

https://developers.google.com/gmail/api/reference/rest/v1/users/getProfile
