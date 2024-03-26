const protocol = import.meta.env.VITE_PROTOCOL;
const key = import.meta.env.VITE_SERVER_IP;
const port = import.meta.env.VITE_PORT;
const BACKEND_API_URL = protocol + key + ":" + port + "/api/v1";

export default BACKEND_API_URL;
