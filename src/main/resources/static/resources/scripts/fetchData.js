export default async function fetchData(url, maxRetries) {
	let retryCount = 0;
	while (retryCount < maxRetries) {
		try {
			const response = await fetch(url);

			if (!response.ok) {
				throw new Error(`Network response was not ok: ${response.statusText}`);
			}

			const data = await response.json();
			return data;
		} catch (error) {
			console.error('Fetch error:', error);

			// Retry after a half a second delay
			retryCount++;
			console.log(`Retrying (attempt ${retryCount})...`);
			await new Promise(resolve => setTimeout(resolve, 500));
		}
	}
	
	throw new Error('Max retries reached. Unable to fetch data.');
}
