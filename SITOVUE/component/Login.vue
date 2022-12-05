<template>
<img class="logo" src="../assets/imgLogin.png"/>
    <h1>Login</h1>
    <div class="Login">
        <input type="text" v-model="email" placeholder="Enter Email"/>
        <input type="text" v-model="password" placeholder="Enter Password"/>
        <button v-on:click="LogUp">Login</button>
    </div>
    <br/>
    <br/>
    <router-link to="/HomeNoLogin">accedi senza Login</router-link>
</template>


<script>
import axios from "axios"
import config from "./configuration"

export default {
    name : 'myLogin',
    data()
    {
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            email:'',
            password:'',
            token: ''
        }
    },
    methods:{
        LogUp()
        {
            let self = this

            //parte aggiunta per far funzionare mommentaneamente la vista user e admin per i problemi con XAMPP
            /* if(self.email === "user" && self.password === "user"){
                config.GlobalUsername = self.email
                    config.GlobalPassword = self.password
                    config.GlobalLogin = true
                    config.GlobalAdmin = false
                    self.$router.push({name:'Home'})
            }else{
                config.GlobalUsername = self.email
                    config.GlobalPassword = self.password
                    config.GlobalLogin = true
                    config.GlobalAdmin = true
                    self.$router.push({name:'HomeAdmin'})
            } */

            axios.post(this.MyUrl, {}, {
                params: {
                    action: "getToken"
                },
            }).then(function(response){
                self.token = response.data
                localStorage.setItem('token', response.data)
            })

            if(self.email === "" && this.password === ""){
                alert("Per favore, non lasciare campi vuoti");
            }else{
                
            axios.post(this.MyUrl,{}, {
                params: {
                    action : "login",
                    username: this.email,
                    password: this.password
                },
            }).then(function(response){
                if(response.data === "user"){
                    self.dati = response.data
                    config.GlobalUsername = self.email
                    config.GlobalPassword = self.password
                    config.GlobalLogin = true
                    config.GlobalAdmin = false
                    self.$router.push({name:'Home'})
                }else if (response.data === "admin"){
                    config.GlobalUsername = self.email
                    config.GlobalPassword = self.password
                    config.GlobalLogin = true
                    config.GlobalAdmin = true
                    self.$router.push({name:'HomeAdmin'})
                }else{
                    alert("Error because : " + response.data)
                }
            })
            }
        }
        
            
    }
}
//VEDERE LA PARTTE DI COLLEGAMENTO AD UNA SERVLET
</script>
<style>
body{
   padding: 0;
   margin: 0;
}
.logo{
    width: 100px
}
.Login input{
     width: 300px;
     height: 40px;
     padding-left: 20px;
     display: block;
     margin-bottom: 30px;
     margin-right: auto;
     margin-left: auto;
     border: 1px solid bisque;

}
.Login button{
    width: 320px;
    height: 40px;
    border: 1px solid skyblue;
    background-color: skyblue;
}
</style>