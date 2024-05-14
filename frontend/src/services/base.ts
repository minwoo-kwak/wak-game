import axios from 'axios';
import { BASE_URL, getAccessToken } from '../constants/api';

export const axiosInstance = axios.create({
  baseURL: BASE_URL,
});

axiosInstance.interceptors.request.use(
  (config) => {
    const ACCESS_TOKEN = getAccessToken();
    if (ACCESS_TOKEN) {
      config.headers['Authorization'] = `Bearer ${ACCESS_TOKEN}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
