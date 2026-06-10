document.getElementById('cadastroForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const nome = document.getElementById('nameInput').value;
    const email = document.getElementById('emailInput').value;
    const password = document.getElementById('passwordInput').value;
    const errorMessage = document.getElementById('error-message');

    if (nome.trim() === "" || password.trim() === "" || !email.includes("@")) {
        this.reset();
        errorMessage.textContent = "Erro: O nome e a senha não podem estar vazios, e o e-mail deve ser válido.";
        errorMessage.style.display = "block";
        return;
    }

    const userData = {
        nome: nome,
        email: email,
        password: password
    };
    /*
        Mesma coisa do login se você leu la primeiro, vou enviar o arquivo JSON com os dados: nome, email e senha;
        no JAVA tu vai fazer a verificação de que o codigo não tem SQL injection, e se o dados é validos, no JS tem
        verificação minima (EX. nome vazio e falta de @), ai tu envia uma response ok ou não
    */
    try {
        const response = await fetch("http://localhost:8080/api/cadastro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        });

        if (response.ok) {
            window.location.href = "login.html";
        } else {
            this.reset();
            errorMessage.textContent = "Erro ao realizar cadastro. Tente novamente.";
            errorMessage.style.display = "block";
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        this.reset();
        errorMessage.textContent = "Erro de conexão com o servidor.";
        errorMessage.style.display = "block";
    }
});