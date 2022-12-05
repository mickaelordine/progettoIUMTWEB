import Home from './components/Home.vue'
import Login from './components/Login.vue'
import History from './components/History.vue'
import HomeNoLogin from './components/HomeNoLogin.vue'
import HomeAdmin from './components/HomeAdmin.vue'
import IUM from './components/IUM.vue'
import TWEB from './components/TWEB.vue'
import Reti from './components/Reti.vue'
import Prog3 from './components/Prog3.vue'
import Prog2 from './components/Prog2.vue'
import HistoryAdmin from './components/HistoryAdmin.vue'
import Tutti from './components/Tutti.vue'
import {createRouter,createWebHashHistory} from 'vue-router'

const routes=[
    {
        name:'Home',
        component:Home,
        path:'/Home'
    },
    {
        name:'Login',
        component:Login,
        path:'/'
    },
    {
        name:'History',
        component:History,
        path:'/History'
    },
    {
        name:'HomeNoLogin',
        component:HomeNoLogin,
        path:'/HomeNoLogin'
    },
    {
        name:'HomeAdmin',
        component:HomeAdmin,
        path:'/HomeAdmin'
    },
    {
        name:'IUM',
        component:IUM,
        path:'/IUM'
    },
    {
        name:'TWEB',
        component:TWEB,
        path:'/TWEB'
    },
    {
        name:'Prog3',
        component:Prog3,
        path:'/Prog3'
    },
    {
        name:'Prog2',
        component:Prog2,
        path:'/Prog2'
    },
    {
        name:'Reti',
        component:Reti,
        path:'/Reti'
    },
    {
        name:'HistoryAdmin',
        component:HistoryAdmin,
        path:'/HistoryAdmin'
    },
    {
        name:'Tutti',
        component:Tutti,
        path:'/Tutti'
    }
];

const router=createRouter({
    history:createWebHashHistory(),
    routes
});

export default router;