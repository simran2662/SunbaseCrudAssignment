const apiUrl = 'http://localhost:8080';

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();  // Prevent default form submission
    
    const email = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch(`${apiUrl}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to login');  // Handle non-200 status
        }
        return response.json();
    })
    .then(result => {
        if (result.success) {
			console.log(result.token);
            localStorage.setItem('token', result.token);
            // Redirect to customer page on successful login
            //window.location.href = '/customer/getCustomer';
            window.location.href = '/customer.html';
        } else {
            document.getElementById('error').innerText = 'Invalid username or password';
        }
    })
    .catch(error => {
        console.error('Error during login:', error);
        document.getElementById('error').innerText = 'Invalid username or password';
    });
});
