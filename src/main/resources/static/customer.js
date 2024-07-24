// Define the API base URL
const apiUrl = 'http://localhost:8080';

// Array to hold customer data fetched from the API
let customers = [];

function checkAuthentication() {
	const token = localStorage.getItem('token');
	if (!token) {
		window.location.href = '/index.html'; // Redirect to login page if token is not found
	}
}

function handleFetchResponse(response) {
	if (response.status === 401) {
		// Token is invalid or expired, redirect to login page
		localStorage.removeItem('token'); // Optionally remove the token from local storage
		window.location.href = '/index.html';
		throw new Error('Unauthorized');
	}
	return response;
}
// Function to fetch customer data from the backend API
function fetchCustomerData(sortField, sortOrder, commonSearch) {
	checkAuthentication();
	const token = localStorage.getItem('token'); // Retrieve JWT token from localStorage
	let endpoint = `${apiUrl}/customer/getCustomer`;
	if (sortField && sortOrder) {
		endpoint += `?sort=${sortField}&sortOrder=${sortOrder}`;
	}
	if (commonSearch) {
		endpoint += (sortField && sortOrder) ? `&commonSearch=${commonSearch}` : `?commonSearch=${commonSearch}`;
	}
	// Fetch customer data from the backend API
	fetch(endpoint, {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ${token}`, // Include JWT token in the headers for authentication
			'Content-Type': 'application/json'
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to fetch customer data');
			}
			handleFetchResponse(response.json);
			return response.json();
		})
		.then(data => {
			console.log('Customer Data:', data);
			customers = data; // Store fetched customer data in the local array
			renderCustomerTable(customers); // Render the customer data in the HTML table
		})
		.catch(error => {
			console.error('Error fetching customer data:', error);
			// Handle error scenarios, such as displaying an error message to the user
		});
}

// Function to render the customer data in an HTML table
function renderCustomerTable(customers) {
	const tableBody = document.getElementById('customerData');
	tableBody.innerHTML = ''; // Clear existing table rows

	// Loop through each customer object and create a new row in the table
	customers.forEach(customer => {
		const row = document.createElement('tr');
		row.innerHTML = `
            <td>${customer.id}</td>
            <td contenteditable="true">${customer.firstName}</td>
            <td contenteditable="true">${customer.lastName}</td>
            <td contenteditable="true">${customer.street || ''}</td>
            <td contenteditable="true">${customer.address}</td>
            <td contenteditable="true">${customer.city}</td>
            <td contenteditable="true">${customer.state}</td>
            <td contenteditable="true">${customer.email}</td>
            <td contenteditable="true">${customer.phone}</td>
            <td>
                <button onclick="editCustomer(${customer.id})">Edit</button>
                <button onclick="deleteCustomer(${customer.id})">Delete</button>
            </td>
        `;
		tableBody.appendChild(row); // Append the new row to the table body
	});
}

// Function to enable editing for a specific customer
function editCustomer(customerId) {
	checkAuthentication();
	window.location.href = `/addCustomer.html?customerId=${customerId}`;
}

// Function to populate the form with customer data for editing
function populateForm(customer) {
	document.getElementById('customerId').value = customer.id;
	document.getElementById('firstName').value = customer.firstName || '';
	document.getElementById('lastName').value = customer.lastName || '';
	document.getElementById('street').value = customer.street || '';
	document.getElementById('address').value = customer.address || '';
	document.getElementById('city').value = customer.city || '';
	document.getElementById('state').value = customer.state || '';
	document.getElementById('email').value = customer.email || '';
	document.getElementById('phone').value = customer.phone || '';
}

// Function to fetch customer data by ID
function fetchCustomerById(customerId) {
	checkAuthentication();
	const token = localStorage.getItem('token'); // Retrieve JWT token from localStorage

	fetch(`${apiUrl}/customer/getCustomer?id=${customerId}`, {
		method: 'GET',
		headers: {
			'Authorization': `Bearer ${token}`, // Include JWT token in the headers for authentication
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
			console.log('Fetched Customer Data:', data);
			populateForm(data);
		})
		.catch(error => {
			console.error('Error fetching customer data:', error);
			// Handle error scenarios, such as displaying an error message to the user
		});
}

// Function to delete a customer based on customer ID
function deleteCustomer(customerId) {
	checkAuthentication();
	const token = localStorage.getItem('token'); // Retrieve JWT token from localStorage

	// Send a DELETE request to delete customer data on the server
	fetch(`${apiUrl}/customer/${customerId}`, {
		method: 'DELETE',
		headers: {
			'Authorization': `Bearer ${token}` // Include JWT token in the headers for authentication
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to delete customer');
			}
			fetchCustomerData(null, null, null); // Refresh customer data after successful deletion
			location.reload();
			return response.json();
		})
		.then(data => {
			console.log('Customer deleted successfully:', data);
		})
		.catch(error => {
			console.error('Error deleting customer:', error);
			// Handle error scenarios, such as displaying an error message to the user
		});
}

// Example function to add a new customer (dummy data for demonstration)
function addNewCustomer() {
	checkAuthentication();
	// Get form input values
	const firstName = document.getElementById('firstName').value;
	const lastName = document.getElementById('lastName').value;
	const address = document.getElementById('address').value;
	const city = document.getElementById('city').value;
	const state = document.getElementById('state').value;
	const email = document.getElementById('email').value;
	const phone = document.getElementById('phone').value;

	// Create new customer object with form input data
	const newCustomer = {
		firstName: firstName,
		lastName: lastName,
		address: address,
		city: city,
		state: state,
		email: email,
		phone: phone
	};

	const token = localStorage.getItem('token'); // Retrieve JWT token from localStorage

	// Send a POST request to add a new customer on the server
	fetch(`${apiUrl}/customer/saveOrUpdate`, {
		method: 'POST',
		headers: {
			'Authorization': `Bearer ${token}`, // Include JWT token in the headers for authentication
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(newCustomer) // Convert newCustomer object to JSON string
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to add new customer');
			}
			return response.json();
		})
		.then(data => {
			console.log('New customer added successfully:', data);
			fetchCustomerData(null, null,null); // Refresh customer data after successful addition
		})
		.catch(error => {
			console.error('Error adding new customer:', error);
			// Handle error scenarios, such as displaying an error message to the user
		});
}

// Event listener for sorting customers based on order selection
document.getElementById('sortField').addEventListener('change', function() {
	const sortField = this.value; // Get the selected sort field
	const sortOrder = document.getElementById('sortOrder').value; // Get the selected sort order
	fetchCustomerData(sortField, sortOrder, null); // Fetch customer data with sorting parameters
});

document.getElementById('sortOrder').addEventListener('change', function() {
	const sortField = document.getElementById('sortField').value; // Get the selected sort field
	const sortOrder = this.value; // Get the selected sort order
	fetchCustomerData(sortField, sortOrder, null); // Fetch customer data with sorting parameters
});
function debounce(func, delay) {
	let debounceTimer;
	return function() {
		const context = this;
		const args = arguments;
		clearTimeout(debounceTimer);
		debounceTimer = setTimeout(() => func.apply(context, args), delay);
	};
}
document.getElementById('commonSearchKey').addEventListener('input', debounce(function() {
	const commonSearch = this.value;
	fetchCustomerData(null, null, commonSearch);
}, 300));

// Initialize the customer data fetching when the page loads
document.addEventListener('DOMContentLoaded', fetchCustomerData);

// Check if we are on the addCustomer.html page and if there is a customerId in the URL
if (window.location.pathname === '/addCustomer.html') {
	const urlParams = new URLSearchParams(window.location.search);
	const customerId = urlParams.get('customerId');
	if (customerId) {
		fetchCustomerById(customerId); // Fetch and populate customer data if editing
	}
}
