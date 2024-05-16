export const BASE_URL = process.env.REACT_APP_BASE_URL;
export const CHAT_URL = process.env.REACT_APP_CHAT_URL;

export const getAccessToken = () => {
  const userStore = sessionStorage.getItem('userStore');
  const token = userStore && JSON.parse(userStore).state.userData.token;
  return token;
};
