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

**Choose "Web application" as the application type.**

**Choose "User data"**



![Screenshot from 2024-09-07 12-51-15](https://github.com/user-attachments/assets/00cb5ebc-415e-45e2-a2e6-1afa7b5fd562)

![Screenshot from 2024-09-07 12-52-31](https://github.com/user-attachments/assets/35b076ff-027b-4447-88ef-1ee43c3539ab)


![Screenshot from 2024-09-03 20-47-26](https://github.com/user-attachments/assets/2f283c62-c4fc-4d37-b1a7-2f69f8488138)


![Screenshot from 2024-09-03 19-28-31](https://github.com/user-attachments/assets/eed1127e-12cd-4dc7-aa8a-f80c7e46406c)


### 6- OAuth Consent Screen

**write your data in Oauth Consent Screet** 

 **1- APP information :** 
 The name of the app asking for consent
 
**2- User support email :**
    The User Support Email field requires you to specify an email address that will be displayed to users on the consent   screen.  This should be an email address that you regularly monitor so that you can answer questions they have about sign-in

   **3-  Developer contact information**
    These email addresses are for Google to notify you about any changes to your project. 
    
 and click `SAVE AND CONTINUE`

### 7-Scopes

**In the OAuth consent screen, click "Add or Remove Scopes".
**Add the following scopes:**
 ```pach
  https://mail.google.com/
 https://www.googleapis.com/auth/gmail.readonly
```
![Screenshot from 2024-09-03 20-13-51](https://github.com/user-attachments/assets/08625df8-be93-4133-930d-46b4313358bc)



![Screenshot from 2024-09-03 20-28-00](https://github.com/user-attachments/assets/5266fbb2-1269-4ca3-be2d-1e8cb5072b9d)


 ### 8-Create OAuth Client
 ![Screenshot from 2024-09-04 08-02-49](https://github.com/user-attachments/assets/caef98fa-c0e6-4bbb-8771-9b71922806c5)


**Authorized JavaScript origins :** ``` http://localhost:8080 ```

**Authorized redirect URIs :**  ``` http://localhost:8080/redirect ```

![Screenshot from 2024-09-04 08-31-32](https://github.com/user-attachments/assets/f40dfdc8-2e6d-4336-8b29-d8db56aff19b)


### 9- Configure OAuth Consent Screen:

**1-In "APIs & Services", go to "OAuth consent screen".**

**2-In the "Test users" section, add the email addresses that will be used to test the application:**
 
  ![Screenshot from 2024-09-04 09-09-13](https://github.com/user-attachments/assets/2a0676f5-7cef-4d2f-ba87-12d5da729ad3)

### 10-Create a Pub/Sub topic:

**1- In the Google Cloud Console, go to the Pub/Sub section.**

**2- Click on "Create Topic".**

**3- Enter a name for your topic (e.g., "email-notifications").**

**4- Add `Topic name` that you created in a project in file `GoogleProperties`
    private String topicName ="projects/mostafa-project-434605/topics/email-notifications" ;**
![Screenshot from 2024-09-04 10-31-48](https://github.com/user-attachments/assets/c375a8ea-296f-4938-9220-df67413af3e6)


### 11-Subscription
Create and configure a subscription for your topic:

**1- In the Pub/Sub section, find your newly created topic.**

**2- Click on the topic name to view its details.**

**3- In the "Subscriptions" tab, click "Create Subscription".**

**4- Enter a name for your subscription (e.g., "email-notification-sub").**

**5- Under "Delivery Type", select "Push".**

**6- For the Endpoint URL, you'll need a public URL that can receive messages from Pub/Sub. We'll use ngrok for this**

**8-Click "Create" to finalize the subscription creation.**

![Screenshot from 2024-09-04 10-23-11](https://github.com/user-attachments/assets/39b79519-6d2d-4963-8a57-d53856d093fd)

 ![Screenshot from 2024-09-04 10-22-23](https://github.com/user-attachments/assets/eba6c7b7-df9b-4947-83fc-7caaf2f46601)
 

![Screenshot from 2024-09-04 10-21-31](https://github.com/user-attachments/assets/3abb25ef-3a44-4a6b-a036-ae36d0450e98)

## 2. Application Configuration

After setting up Google Cloud, configure your application:

**Copy your `Client ID` , `Secret ID` , `Topic Name` from the Google Cloud Console to your project **

**Client Id , Topic Name --> add in `yml.file`.**

**Topic Name add in `GoogleProprtties`**

( CREDENTIALS > `web client 1` >  information and summary  )**
Update the `application.yml` file with your Client ID ( in the project ).

  ![Screenshot from 2024-09-03 20-35-07](https://github.com/user-attachments/assets/50293595-3fe9-443f-bf4d-36507255602f)


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
