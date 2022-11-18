---
title: vue3_通用后台项目
date: 2022-11-18 20:42:01
tags:
- vue3_通用后台项目
categories: 
- 练手项目
---

# Axios二次封装

`utils/request.js` 文件

```js
/**
 * axios 二次封装
 */
import axios from 'axios';
import {
	ElMessage
} from 'element-plus';
import router from '../router/index.js';

// 定义常见的错误信息
const TOKEN_ERROR = 'Token认证失败，请重新登录';
const NETWORK_ERROR = '网络异常，请检查网络后重试';

// 创建axios实例对象，添加全局配置
const service = axios.create({
	baseURL: process.env.VUE_APP_BASE_API,
	timeout: 8000
});

// 请求拦截
service.interceptors.request.use((config) => {
	// TODO
	const headers = config.headers;
	if (!headers.Authorization) {
		headers.Authorization = 'Bear Test'
	}
	return config;
});

// 响应拦截
service.interceptors.response.use((config) => {
	const {
		code,
		data,
		msg
	} = config.data;
	if (code === 200) {
		return data;
	} else if (code === 40001) { // 和后台约定的，Token 过期，code是40001
		ElMessage.error(TOKEN_ERROR);
		setTimeout(()=>{
			router.push({ path: '/login' })
		}, 1000);
		return Promise.reject(TOKEN_ERROR);
	} else {
		ElMessage.error(msg || NETWORK_ERROR);
		return Promise.reject(msg || NETWORK_ERROR);
	}
});

/**
 * 请求核心函数
 * @param {Object} options 请求配置
 */
function request(options) {
	options.method = options.method || 'get';  // 如果是get请求的话，则将data中的数据转移到params中
	if(options.method.toLowerCase() === 'get') {
		options.params = options.data;
	}
	
	if(process.env.NODE_ENV === 'production') {  // 在正式环境中，不管有没有开启moke开关，都使用正式环境base_url，以防万一
		service.defaults.baseURL = process.env.VUE_APP_BASE_API;
	} else { // VUE_APP_MOCK为1，代表开启moke模式
		service.defaults.baseURL = process.env.VUE_APP_MOCK == '1' ? process.env.VUE_APP_MOCK_API : process.env.VUE_APP_BASE_API;
	}
	
	return service(options);
}

/**
 * 可以通过 request.get() 方式调用
 */
['get', 'post', 'put', 'delete', 'patch'].forEach((item)=>{
	request[item] = (url, data, options)=>{
		return request({
			method: item,
			url, 
			data, 
			...options
		})
	}
});

export default request;

```

## 全局注册

`main.js` 文件中

```js
import request from './utils/request.js'

app.config.globalProperties.$request = request;
```

## 调用

```js
import {getCurrentInstance} from 'vue'

const {proxy} = getCurrentInstance();

//this.$request({//vue2
proxy.$request({//vue3
	method: 'get',
	url: '/login',
	data: {
		name: 'gongxin'
	}
}).then(res => {
	console.log('[ 请求 ]', res);
})

```