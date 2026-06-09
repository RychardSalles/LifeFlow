document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('error-message');

    // Verificação de campos: E-mail com @ e senha não vazia
    if (!email.includes("@") || password.trim() === "") {
        this.reset(); // Limpa o formulário
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
            // Sucesso: Redireciona para o dashboard
            window.location.href = "dashboard.html";
        } else {
            // Erro na API: Limpa o formulário e exibe mensagem
            this.reset();
            errorMessage.textContent = "E-mail ou senha incorretos. Tente novamente.";
            errorMessage.style.display = "block";
        }
    } catch (error) {
        // Erro de conexão/Catch: Limpa o formulário e exibe mensagem
        console.error("Erro na requisição:", error);
        this.reset();
        errorMessage.textContent = "Erro de conexão com o servidor. Tente mais tarde.";
        errorMessage.style.display = "block";
    }
});