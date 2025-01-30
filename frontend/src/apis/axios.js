import axios from "axios";

// Axios 기본 설정
const apiClient = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true,
});

// 엑세스 토큰 저장
export const saveAccessToken = function(accessToken){
    sessionStorage.setItem("accessToken", accessToken);
}

// 엑세스 토큰 삭제
export const removeAccessToken = function(){
    sessionStorage.removeItem("accessToken");
}

// 요청 인터셉터 추가
apiClient.interceptors.request.use(
    (config) => {
        const accessToken = sessionStorage.getItem("accessToken");

        if(accessToken){
            config.headers["Authorization"] = accessToken;
        }

        return config;
    },
    (error) => {
        console.error("Request Error: ", error);
        return Promise.reject(error);
    }
);

// 로그인 함수
export const login = async function(endpoint, loginDto){
    try{
        const response = await apiClient.post(endpoint, loginDto);
        if(response.data.statusCode === 200) saveAccessToken(response.headers['x-access-token']);
        return response.data;
    }catch(error){
        return error
    }
}

// get 요청
export const get = async function(endpoint){
    try{
        const response = await apiClient.get(endpoint);
        return response.data;
    }catch(error){
        return error
    }
}

// post 요청
export const post = async function(endpoint, requestData){
    try{
        const response = await apiClient.post(endpoint, requestData);
        return response.data;
    }catch(error){
        return error
    }
}

// put 요청
export const put = async function(endpoint, requestData){
    try{
        const response = await apiClient.put(endpoint, requestData);
        return response.data;
    }catch(error){
        return error
    }
}

// delete 요청
export const del = async function(endpoint){
    try{
        const response = await apiClient.delete(endpoint);
        return response.data;
    }catch(error){
        return error
    }
}