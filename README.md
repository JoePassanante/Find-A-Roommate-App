# Find-A-Roommate-App
Android app used for finding roommates that have similar traits!  Developed for Quinnipiac's Software Engineering Course going over Android Development.

Developed by Alexandra Sazhin and Joe Passanante

## Technologies Used

### Firebase Auth
We use Firebase Auth to verify user identities, and to assign User ID codes. Using libraries given by Google, this User ID is automatically sent with any request to firebase services. This allows us to verify if a user account exists, if they have a profile filled out, and basic security measures.

### Firebase Storage
Firebase storage is used to store User Profile images. When a user creates an account or chooses to upload a new profile it is saved under their username folder in storage, where other users can recover the image!

### Firbase Realtime Database
Firebase Database (No-SQL) allows us to store User Classes as JSON that can easily converted back to a java class. Using this technology, we store user information, matches, and match references. 
