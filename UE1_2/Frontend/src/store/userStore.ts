import { defineStore } from "pinia";
import router from "@/router";

export const useUserStore = defineStore("userStore", {
    state: () => ({
        isLoggedIn: false,
        roles: [] as string[],
        userName: ""
    }),

    actions: {
        async login(username: string, password: string) {

            const params = new URLSearchParams();
            params.append("username", username);
            params.append("password", password);

            const response = await fetch("http://localhost:8080/api/users/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params,
                credentials: "include"
            });
            const json = await response.json();

            console.log("Response:", response);
            if (response.ok) {
                this.userName = json.username;
                this.isLoggedIn = true;
                this.roles = json.roles;

                console.log(this.userName,this.roles);

                router.push("/");
            }
        },

        async checkLogin(strict : boolean) {
            const response = await fetch("http://localhost:8080/api/users/checkLogin", {
                credentials: "include"
            });
            if(!response.ok) {
                this.isLoggedIn = false
                if(strict){
                    router.push("/login");
                }
                return;
            }
            const json = await response.json();

            this.isLoggedIn = response.ok;
            this.userName = json.username;
            this.roles = json.roles;
        },

        async logout() {
           const response = await fetch("http://localhost:8080/api/users/logout", {
                method: "POST",
                credentials: "include"
            });

           if(response.ok) {
               this.isLoggedIn = false;
               this.userName = "";
               this.roles = [];
           }


        }
    }
});