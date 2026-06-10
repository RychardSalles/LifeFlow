
const isInsidePaginas = window.location.pathname.includes('/paginas/');
const isHomePage = !isInsidePaginas;

if (!sessionStorage.getItem('meu_jwt') && !isHomePage) {
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
        console.warn("Backend offline, usando dados de teste (Mock).", error);
        
        if (url.includes('/api/dashboard/chart-data')) {
            return {
                ok: true,
                json: async () => ({
                    labels: ["Teste A", "Teste B", "Teste C"],
                    values: [40, 30, 30]
                })
            };
        }
        
        throw error;
    }
}
