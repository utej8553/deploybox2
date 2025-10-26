DeployBox - Simple Frontend

This is a minimal static frontend that implements a Signup, Create Project and File/ZIP Upload UI for a static project deployment platform.

Files added:
- `index.html` - main page with the three stacked sections (Signup → Create Project → Upload File)
- `styles.css` - minimal styles
- `app.js` - JavaScript handling POST requests to the backend and showing messages

Usage:
1. Open `index.html` in your browser (double-click or serve with a static server).
2. Signup: fills username, email, password → POST http://localhost:8080/api/users/signup (application/x-www-form-urlencoded)
3. Login: fill username and password → POST http://localhost:8080/api/users/login (application/x-www-form-urlencoded). Successful login stores the username locally and auto-fills the other forms.
4. Create Project: fills projectName (username auto-filled when logged in) → POST http://localhost:8080/api/projects/create (application/x-www-form-urlencoded)
5. Upload: fills projectName and chooses a .zip or .html/.css/.js files (username auto-filled when logged in) → POST http://localhost:8080/api/deploy/upload (multipart/form-data). Upload shows progress and displays the project URL on success.

Notes & CORS:
Notes & CORS:
- The frontend is configured to talk to a local Spring Boot backend at `http://localhost:8080` for signup, login, create and upload endpoints. If your backend is hosted elsewhere, update the URLs in `app.js` and ensure the backend allows CORS from the frontend origin.

Bonus:
Bonus:
- After a successful upload the UI displays a constructed project URL:
  `http://<server-ip_or_host>/<username>/<projectName>/`
  The UI currently constructs the link using `http://localhost:8080` as the server host (you can edit `app.js` if your files are served from a different address).

Testing:
- This is a static frontend. No build step required.
For local testing with a backend running at `http://localhost:8080`, serve this folder and open the page in a browser. Example using Python's simple server:

```powershell
# from c:\Users\AVULLA UTEJ\Downloads\deploybox-frontend
python -m http.server 8000
# then open http://localhost:8000 in your browser
```
