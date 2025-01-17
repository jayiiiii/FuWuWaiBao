import axios from 'axios'
import ElementUI from "element-ui";
import {push} from "@/router";

const request = axios.create({
    baseURL: 'http://localhost:9090',  // 注意！！ 这里是全局统一加上了前缀，也就是说所有接口都会加上http://localhost:9090前缀在，页面里面写接口的时候就不要加了，否则会出现2个http://localhost:9090，报错，切记！！！
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let staff = localStorage.getItem("staff") ? JSON.parse(localStorage.getItem("staff")) : null
    if (staff){
        config.headers['token']=staff.token;// 设置请求头

    }
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {//文件对象
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res//转化为json对象
        }
        //当权限验证不通过时候，给出提示
        if(res.code==='401'){
            ElementUI.Message({
                message:res.msg,
                type:'error'
                }
            );
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

