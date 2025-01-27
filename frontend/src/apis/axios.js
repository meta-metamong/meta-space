import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/api";
axios.defaults.withCredentials = true;

// 엑세스 토큰 저장
export const saveAccessToken = function(accessToken){
    sessionStorage.setItem("accessToken", accessToken);
}

// 엑세스 토큰 획득
const getAccessToken = function(){
    return sessionStorage.getItem("accessToken");
}

// 인증이 필요한 Get 요청
export const authenticatedGet = async function(endpoint){
    try {
        const response = await axios.get(endpoint, {
            headers: {
                'X-AUTH-TOKEN': getAccessToken()
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error:', error);
    }
}

// 인증이 필요하지 않은 Get 요청
export const unauthenticatedGet = async function(endpoint){
    try {
        const response = await axios.get(endpoint);
        return response.data;
    } catch (error) {
        console.error('Error:', error);
    }
}

// 인증이 필요한 Post 요청
export const authenticatedPost = async function(endpoint, requestData){
    try {
        const response = await axios.post(endpoint, {
            headers: {
                'X-AUTH-TOKEN': getAccessToken()
            }
        },requestData);
        return response.data;
    } catch (error) {
        console.error('Error:', error);
    }
}

// 인증이 필요하지 않은 Post 요청
export const unauthenticatedPost = async function(endpoint, requestData){
    try {
        const response = await axios.post(endpoint, requestData);
        return response.data;
    } catch (error) {
        console.error('Error:', error);
    }
}