
const maxRetries = 3;
let retryCount = 0;

async function fetchData(url) {
	while (retryCount < maxRetries) {
		try {
			const response = await fetch(url);

			if (!response.ok) {
				throw new Error(`Network response was not ok: ${response.statusText}`);
			}

			const data = await response.json();
			retryCount = 0;
			return data;
		} catch (error) {
			console.error('Fetch error:', error);

			// Retry after a half a second delay
			retryCount++;
			console.log(`Retrying (attempt ${retryCount})...`);
			await new Promise(resolve => setTimeout(resolve, 500));
		}
	}
	
	retryCount = 0;
	throw new Error('Max retries reached. Unable to fetch data.');
}
