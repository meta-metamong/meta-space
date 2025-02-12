import axios from "axios";
import store from '../store/index';

// Axios 기본 설정
const apiClient = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}/api`,
    withCredentials: true
});

// 엑세스 토큰 저장
export const saveAccessToken = function(response){
    sessionStorage.setItem("accessToken", response.headers['x-access-token']);
}

// 엑세스 토큰 삭제
export const removeAccessToken = function(){
    sessionStorage.removeItem("accessToken");
}

// 요청 인터셉터 추가
apiClient.interceptors.request.use(
    (config) => {
        const accessToken = sessionStorage.getItem("accessToken");

        if(accessToken && (store.state.userId !== null || store.state.userId !== undefined)){
            config.headers["Authorization"] = 'bearer ' + accessToken;
        }

        return config;
    },
    (error) => {
        console.error("Request Error: ", error);
        return error;
    }
);

// 응답 인터셉터 추가
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const {config, response} = error;
        const isReissuable = (error.status === 401 && (response.data?.message === "토큰 존재" || response.data?.message === "만료된 토큰"));
        if(!isReissuable) return error;

        if(response.data.message === "토큰 존재"){
            delete config.headers["Authorization"];
            removeAccessToken();
        }else{
            await reissue();
        }
        
        return apiClient(config);
    }
);

// 토큰 재발행 함수
export const reissue = async function(){
    try{
        const response = await get("/members/reissue");
        if(response.status === 200) {
            saveAccessToken(response);
        }
    }catch(error){
        return error;
    }
}

// 로그인 함수
export const login = async function(loginDto){
    try{
        const response = await post("/members/login", loginDto);
        if(response.status === 200) saveAccessToken(response);
        return response;
    }catch(error){
        return error;
    }
}

// 로그아웃 함수
export const logout = async function(){
    try{
        const response = await post("/members/logout", null);
        if(response.status === 200) removeAccessToken();
        return response;
    }catch(error){
        return error;
    }
}

// get 요청
export const get = async function(endpoint){
    try{
        const response = await apiClient.get(endpoint);
        return response;
    }catch(error){
        return error
    }
}

// post 요청
export const post = async function(endpoint, requestData){
    try{
        const response = await apiClient.post(endpoint, requestData);
        return response;
    }catch(error){
        return error
    }
}

// put 요청
export const put = async function(endpoint, requestData){
    try{
        const response = await apiClient.put(endpoint, requestData);
        return response;
    }catch(error){
        return error
    }
}

// delete 요청
export const del = async function(endpoint){
    try{
        const response = await apiClient.delete(endpoint);
        return response;
    }catch(error){
        return error
    }
}

export default apiClient;