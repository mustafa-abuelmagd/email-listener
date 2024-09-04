## Email Listening System

This system listens for incoming emails using Google's Pub/Sub service.
It consists of two main components: Authentication and Email Listening.

### System Overview

**1-Authentication:**

This step is used to obtain a token from Google services. 
The process begins at the endpoint `http://localhost/login`.

**2-Email Listening:** 

After authentication, the system starts listening to a topic in Google's Pub/Sub system based on your configuration in Google Cloud.

### Setup Instructions


**2. Authentication Setup**
To start the authentication process and obtain a token, you'll need the following:

Client ID
Redirect URL
Required scopes

##### Implement these in your Google Cloud project settings:

#### 1- Go to the Google Cloud Platform Console:
`https://console.cloud.google.com/apis/`

#### Create a new project:

**Click on the project dropdown near the top of the page.**

**Select "New Project" or use the search bar to find "New project".**

![Screenshot from 2024-09-03 19-01-48](https://github.com/user-attachments/assets/ad599b87-ef05-4323-a7eb-ecfd29fb3498)


![Screenshot from 2024-09-03 19-07-40](https://github.com/user-attachments/assets/8a1b7ac9-c9e2-4530-82b5-12aaec4f423e)


#### 2- Enable the Gmail API:

**Go to "APIs & Services" > "Library".**

**Search for "Gmail API" and select it**

**Click "Enable".**

![image](https://github.com/user-attachments/assets/2f1015f0-b293-42dc-be0f-e4add7016c2f)


![Screenshot from 2024-09-03 19-12-47](https://github.com/user-attachments/assets/09ca39a1-1057-4e02-a68a-013b1ea31882)

#### 3-Configure OAuth credentials:

**In "APIs & Services", go to "Credentials".**

**Click "Create Credentials" > "OAuth client ID".**

**Choose "Web application" as the application type.**

**Choose "User data"**

##### 4- now you want to `CREATE CREDENTIALS

![Screenshot from 2024-09-03 19-19-17](https://github.com/user-attachments/assets/5d172b0f-1be9-4055-ae59-c75927d5b7c2)

![Screenshot from 2024-09-03 20-47-26](https://github.com/user-attachments/assets/2f283c62-c4fc-4d37-b1a7-2f69f8488138)


![Screenshot from 2024-09-03 19-28-31](https://github.com/user-attachments/assets/eed1127e-12cd-4dc7-aa8a-f80c7e46406c)


#### 5- OAuth Consent Screen

**write your data in Oauth Consent Screet** 

 **1- APP information :** 
 The name of the app asking for consent
 
**2- User support email :**
    The User Support Email field requires you to specify an email address that will be displayed to users on the consent   screen.  This should be an email address that you regularly monitor so that you can answer questions they have about sign-in

   **3-  Developer contact information**
    These email addresses are for Google to notify you about any changes to your project. 
    
 and click `SAVE AND CONTINUE`

#### 6-Scopes

**In the OAuth consent screen, click "Add or Remove Scopes".
**Add the following scopes:**
 ```pach
  https://mail.google.com/
 https://www.googleapis.com/auth/gmail.readonly
```
![Screenshot from 2024-09-03 20-13-51](https://github.com/user-attachments/assets/08625df8-be93-4133-930d-46b4313358bc)



![Screenshot from 2024-09-03 20-28-00](https://github.com/user-attachments/assets/5266fbb2-1269-4ca3-be2d-1e8cb5072b9d)


 #### 5-Create OAuth Client
 ![Screenshot from 2024-09-04 08-02-49](https://github.com/user-attachments/assets/caef98fa-c0e6-4bbb-8771-9b71922806c5)


**Authorized JavaScript origins  :** `http://localhost:8080`

**Authorized redirect URIs :** `http://localhost:8080/login`

![Screenshot from 2024-09-03 20-52-03](https://github.com/user-attachments/assets/fa6f8b25-0f71-4db7-9327-945c6ea5d4a4)









  

## 2. Application Configuration

After setting up Google Cloud, configure your application:

**Copy your Client ID from the Google Cloud Console ( CREDENTIALS > `web client 1` >  information and summary  )**
Update the `application.yml` file with your Client ID ( in the project ).

  ![Screenshot from 2024-09-03 20-35-07](https://github.com/user-attachments/assets/50293595-3fe9-443f-bf4d-36507255602f)

