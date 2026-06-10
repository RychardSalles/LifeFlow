
const isInsidePaginas = window.location.pathname.includes('/paginas/');
const isLoginPage = window.location.pathname.includes('login.html');
const isCadastroPage = window.location.pathname.includes('cadastro.html');

// Uma página é pública se for a Home (fora de /paginas/), Login ou Cadastro
const isPublicPage = !isInsidePaginas || isLoginPage || isCadastroPage;

const token = sessionStorage.getItem('meu_jwt');
const hasValidToken = token && token !== "undefined";

if (!hasValidToken && !isPublicPage) {
    const loginPath = isInsidePaginas ? 'login.html' : 'paginas/login.html';
    window.location.href = loginPath; 
}

function atualizarInterfaceUsuario() {
    const token = sessionStorage.getItem('meu_jwt');
    const nome = sessionStorage.getItem('user_name');
    const authContainer = document.querySelector('.auth-nav') || document.querySelector('.nav-buttons');

    if (token && authContainer) {
        const dashboardPath = isInsidePaginas ? 'dashboard.html' : 'paginas/dashboard.html';

        authContainer.innerHTML = `
            <div class="user-menu-container">
                <span class="user-welcome">Olá, <strong>${nome}</strong></span>
                <div class="user-dropdown">
                    <a href="${dashboardPath}">Dashboard</a>
                    <button onclick="logout()" class="logout-link">Sair</button>
                </div>
            </div>`;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    atualizarInterfaceUsuario();
});

window.addEventListener('pageshow', (event) => {
    atualizarInterfaceUsuario();
});

async function fetchAutenticado(url, opcoes = {}) {
    const token = sessionStorage.getItem('meu_jwt');

    const configuracao = {
        ...opcoes,
        headers: {
            'Content-Type': 'application/json',
            ...opcoes.headers,
            'Authorization': `Bearer ${token}`
        }
    };

    try {
        const resposta = await fetch(url, configuracao);

        if (resposta.status === 401) {
            alert("Sua sessão expirou ou é inválida. Por favor, faça login novamente.");
            
            if (typeof logout === 'function') {
                logout();
            } else {
                sessionStorage.clear();
                window.location.href = isInsidePaginas ? '../index.html' : 'index.html';
            }
        }

        return resposta;
    } catch (error) {
        console.error("Erro na comunicação com o servidor:", error);
        throw error;
    }
}