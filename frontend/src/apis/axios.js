import axios from "axios";

// Axios 기본 설정
const apiClient = axios.create({
    baseURL: "http://localhost:8080/api",
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

        if(accessToken){
            config.headers["Authorization"] = accessToken;
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
        const isReissuable = (error.status === 400 && response.data?.message === "토큰 존재") || (error.status === 401 && response.data?.message === "엑세스 토큰 만료");
        if(!isReissuable) return error;

        if(error.status === 400){
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
        const response = await apiClient.get("/members/reissue");
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
        const response = await post("/members/logout");
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