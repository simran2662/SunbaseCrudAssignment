const apiUrl = 'http://localhost:8080';

// Function to extract URL parameters
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// Function to fetch customer data by ID for editing
function fetchCustomerById(customerId) {
    fetch(`${apiUrl}/customer/getCustomer?id=${customerId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch customer data');
        }
        return response.json();
    })
    .then(data => {
        if (Array.isArray(data) && data.length > 0) {
            const customer = data[0];
            document.getElementById('customerId').value = customer.id || '';
            document.getElementById('firstName').value = customer.firstName || '';
            document.getElementById('lastName').value = customer.lastName || '';
            document.getElementById('street').value = customer.street || '';
            document.getElementById('address').value = customer.address || '';
            document.getElementById('city').value = customer.city || '';
            document.getElementById('state').value = customer.state || '';
            document.getElementById('email').value = customer.email || '';
            document.getElementById('phone').value = customer.phone || '';
        }
    })
    .catch(error => {
        console.error('Error fetching customer data:', error);
        // Handle error scenarios
    });
}

// Check if customerId parameter is present in URL query string
const customerId = getUrlParameter('customerId');
if (customerId) {
    // Fetch customer data for editing
    fetchCustomerById(customerId);
    document.getElementById('saveCustomerBtn').style.display = 'block';
    document.getElementById('addCustomerForm').reset(); // Clear form for new entry
}

// Event listener for form submission (Add/Edit customer)
document.getElementById('addCustomerForm').addEventListener('submit', function(event) {
    event.preventDefault();  // Prevent default form submission
    
    // Get form data
    const formData = {
        id: document.getElementById('customerId').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        street: document.getElementById('street').value || null, // Ensure fields are not undefined
        address: document.getElementById('address').value || null,
        city: document.getElementById('city').value || null,
        state: document.getElementById('state').value || null,
        email: document.getElementById('email').value || null,
        phone: document.getElementById('phone').value || null
    };

    // Determine API endpoint based on whether it's an Add or Edit operation
    const apiUrlEndpoint = `${apiUrl}/customer/saveOrUpdate`;

    // Send POST request to add/edit customer
    fetch(apiUrlEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to save customer');  // Handle non-200 status
        }
        return response.json();
    })
    .then(data => {
        console.log('Customer saved successfully:', data);
        // Redirect to customer list page or perform necessary actions after successful save
        window.location.href = '/customer.html';  // Example: Redirect to customer list page
    })
    .catch(error => {
        console.error('Error saving customer:', error);
        // Handle error scenarios
    });
});

// Event listener for Cancel button
document.getElementById('cancelBtn').addEventListener('click', function() {
    window.location.href = '/customer.html';  // Redirect to customer list page on cancel
});
