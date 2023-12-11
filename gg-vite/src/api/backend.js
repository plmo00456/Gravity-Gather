const key = import.meta.env.VITE_SERVER_IP;
const BACKEND_API_URL = "http://" + key + ":8080/api/v1";

export default BACKEND_API_URL;