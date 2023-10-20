OS:wsl2 ubuntu22.04

DB:postgreSQL

Tool:swagger

Backend: spring boot

Frontend: vue3 + quasar framework  (前端放在client資料夾)

轉帳功能，用戶之間可以互相轉帳，查詢轉帳紀錄，以及自己餘額。



狀態在padding的時候，可以取消轉帳。
![image](https://github.com/lovequ4/spring/blob/main/ScreenShot/transfer01.gif)

轉帳成功
![image](https://github.com/lovequ4/spring/blob/main/ScreenShot/transfer02.gif)

![image](https://github.com/lovequ4/spring/blob/main/ScreenShot/%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2%202023-10-20%20092734.png)

![image](https://github.com/lovequ4/spring/blob/main/ScreenShot/%E8%9E%A2%E5%B9%95%E6%93%B7%E5%8F%96%E7%95%AB%E9%9D%A2%202023-10-20%20094618.png)


API

-----------Transfer------------

POST  /api/transfer       轉帳API

GET   /api/transfer/{Id}  取得轉帳紀錄 (Id =  transferId)  

POST  /api/transfer/{Id}  取消轉帳     (Id =  transferId) 

-----------Accounts------------

POST  /api/accounts/signup  註冊

POST  /api/accounts/signin  登入

GET   /api/accounts/{Id}    取得帳戶餘額
