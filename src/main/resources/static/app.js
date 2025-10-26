// Simple frontend logic for DeployBox UI
(function(){
  function qs(id){ return document.getElementById(id); }
  function showMessage(el, text, isSuccess){ el.textContent = text; el.className = 'message ' + (isSuccess? 'success':'error'); }

  // POST using application/x-www-form-urlencoded and return parsed JSON
  async function postFormUrlEncoded(url, data){
    const body = new URLSearchParams(data).toString();
    const res = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body
    });
    const text = await res.text();
    let json;
    try { json = JSON.parse(text); } catch(e){ json = { message: text }; }
    if(!res.ok) throw { status: res.status, body: json };
    return json;
  }

  document.addEventListener('DOMContentLoaded', ()=>{
    // Signup
    const signupForm = qs('signupForm');
    const signupMsg = qs('signupMsg');
    signupForm.addEventListener('submit', async (e)=>{
      e.preventDefault();
      signupMsg.textContent = '';
      const username = qs('suUsername').value.trim();
      const email = qs('suEmail').value.trim();
      const password = qs('suPassword').value;
      if(!username || !email || !password){ showMessage(signupMsg, 'Please fill all fields', false); return; }
      try{
        await postFormUrlEncoded('http://localhost:8080/api/users/signup', { username, email, password });
        showMessage(signupMsg, 'Signup successful', true);
      }catch(err){
        const msg = (err && err.body && err.body.message) || (err.body && JSON.stringify(err.body)) || 'Signup failed';
        showMessage(signupMsg, msg, false);
      }
    });

    // Login
    const loginForm = qs('loginForm');
    const loginMsg = qs('loginMsg');
    loginForm.addEventListener('submit', async (e)=>{
      e.preventDefault();
      loginMsg.textContent = '';
      const username = qs('liUsername').value.trim();
      const password = qs('liPassword').value;
      if(!username || !password){ showMessage(loginMsg, 'Please enter username and password', false); return; }
      try{
        await postFormUrlEncoded('http://localhost:8080/api/users/login', { username, password });
        showMessage(loginMsg, 'Login successful', true);
        localStorage.setItem('deploybox_username', username);
        qs('cpUsername').value = username;
        qs('upUsername').value = username;
      }catch(err){
        const msg = (err && err.body && err.body.message) || 'Login failed';
        showMessage(loginMsg, msg, false);
      }
    });

    // Create Project
    const createForm = qs('createForm');
    const createMsg = qs('createMsg');
    createForm.addEventListener('submit', async (e)=>{
      e.preventDefault();
      createMsg.textContent = '';
      const username = qs('cpUsername').value.trim();
      const projectName = qs('cpProjectName').value.trim();
      if(!username || !projectName){ showMessage(createMsg, 'Please provide username and project name', false); return; }
      try{
        await postFormUrlEncoded('http://localhost:8080/api/projects/create', { username, projectName });
        showMessage(createMsg, 'Project created successfully', true);
      }catch(err){
        const msg = (err && err.body && err.body.message) || 'Create project failed';
        showMessage(createMsg, msg, false);
      }
    });

    // Upload
    const uploadForm = qs('uploadForm');
    const uploadMsg = qs('uploadMsg');
    const projectLink = qs('projectLink');
    uploadForm.addEventListener('submit', (e)=>{
      e.preventDefault();
      uploadMsg.textContent = '';
      projectLink.textContent = '';
      const username = qs('upUsername').value.trim();
      const projectName = qs('upProjectName').value.trim();
      const fileInput = qs('fileInput');
      const progressEl = qs('uploadProgress');
      if(!username || !projectName){ showMessage(uploadMsg, 'Please provide username and project name', false); return; }
      if(!fileInput.files || fileInput.files.length===0){ showMessage(uploadMsg, 'Please choose a file or ZIP to upload', false); return; }

      const form = new FormData();
      form.append('username', username);
      form.append('projectName', projectName);
      for(let i=0;i<fileInput.files.length;i++) form.append('file', fileInput.files[i]);

      const xhr = new XMLHttpRequest();
      xhr.open('POST', 'http://localhost:8080/api/deploy/upload', true);
      xhr.upload.onprogress = function(evt){
        if(evt.lengthComputable){
          const percent = Math.round((evt.loaded / evt.total) * 100);
          progressEl.style.display = 'block';
          progressEl.value = percent;
        }
      };
      xhr.onload = function(){
        progressEl.style.display = 'none';
        if(xhr.status >=200 && xhr.status < 300){
          showMessage(uploadMsg, 'Upload successful', true);
          const link = 'http://localhost:8080/' + encodeURIComponent(username) + '/' + encodeURIComponent(projectName) + '/';
          projectLink.innerHTML = '<strong>Project URL:</strong> <a href="' + link + '" target="_blank" rel="noopener">' + link + '</a>';
        }else{
          let errText = xhr.responseText || 'Upload failed';
          try{ errText = JSON.parse(errText).message || errText; }catch(e){}
          showMessage(uploadMsg, errText, false);
        }
      };
      xhr.onerror = function(){
        progressEl.style.display = 'none';
        showMessage(uploadMsg, 'Network error during upload', false);
      };
      xhr.send(form);
    });
  });
})();