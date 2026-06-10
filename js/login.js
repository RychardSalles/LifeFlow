document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');

    if (!email.includes("@") || password.trim() === "") {
        this.reset();
        errorMessage.textContent = "Por favor, insira um e-mail válido e preencha a senha.";
        errorMessage.style.display = "block";
        return;
    }

    const userData = {
        email: email,
        password: password
    };
    /*  eu to enviando o email e login para o backend em formato JSON, cria uma pasta api e deixa tudo para fazer request do front la
        dentro da pasta api você vai fazer a confirmação de que o usuario existe e vai mandar uma resposta se ta ok 
        ou não
    */
    try {
        const response = await fetch("http://localhost:8080/api/login", { // <- verifica o localhost certo
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        });

        if (response.ok) {
            const dados = await response.json();

            sessionStorage.setItem('meu_jwt', dados.token);
            sessionStorage.setItem('user_name', dados.nome); // Assume que o Java envia o nome
            sessionStorage.setItem('user_id', dados.id);     // Salva o ID retornado pelo banco
            sessionStorage.setItem('user_email', dados.email); // Opcional: para o perfil
            window.location.href = "dashboard.html";
        } else {
            this.reset();
            errorMessage.textContent = "E-mail ou senha incorretos. Tente novamente.";
            errorMessage.style.display = "block";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        this.reset();
        errorMessage.textContent = "Não foi possível conectar ao servidor. Verifique sua conexão.";
        errorMessage.style.display = "block";
    }
});