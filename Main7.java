import java.util.Random;
import java.util.Scanner;

// ╔═════════════════════════════════════════════════════════════════════╗
// ║              BLOODIVINE RPG — SUMÁRIO DO PROJETO                    ║
// ╠═════════════════════════════════════════════════════════════════════╣
// ║  [MAIN]                → Ponto de entrada do programa               ║// ║                                                                     ║
// ║  [ADMIN]                                                            ║
// ║                                                                     ║
// ║  [UTILITÁRIOS]                                                      ║
// ║    [> Console]         → digitar(), limparTela(), lerOpcaoValida()  ║
// ║                          limparTelaDevagar()                        ║
// ║    [> Cores]           → Constantes ANSI de cor (classe Cores)      ║
// ║                                                                     ║
// ║  [CLASSES RPG]         → Nomes e exibição das classes do jogador    ║
// ║                                                                     ║
// ║  [INIMIGOS]                                                         ║
// ║    [> Constantes]      → Chaves dos inimigos (classe Inimigos)      ║
// ║    [> Modelo]          → Classe Inimigo: criação e ações            ║
// ║                                                                     ║
// ║  [PLAYER]                                                           ║
// ║    [> Modelo]          → Classe Player: ações e buffs               ║
// ║    [> Criação]         → escolhaClasse(), opcaoParaClasse()         ║
// ║    [> Buffs]           → escolherEAplicarBuff()                     ║
// ║                                                                     ║
// ║  [COMBATE]                                                          ║
// ║    [> Iniciativa]      → playerTemIniciativa()                      ║
// ║    [> Crítico]         → calcularDanoCritico()  ← 20–80% extra      ║
// ║    [> Ataques]         → aplicarAtaquePlayer(), aplicarAtaqueInimigo║
// ║    [> Turnos]          → turnoAtaque(), turnoCura()                 ║
// ║    [> Fluxo]           → combate(), executarFase()                  ║
// ║                                                                     ║
// ║  [GRÁFICOS]                                                         ║
// ║    [> HUD]             → mostrarHud(), mostrarStatus()              ║
// ║    [> Arte ASCII]      → imprimirTituloBlooDivine(), mostrarSala()  ║
// ║    [> Salas]                                                        ║
// ║                                                                     ║
// ║  [NARRADOR]                                                         ║
// ║   [> Introdução]       → introducao()                               ║
// ║   [> Pos-batalha]      → pos_batalha()                              ║
// ║   [> Pre-batalha]      → pre_batalha()                              ║
// ║                                                                     ║
// ║  [ADMIN]               → admin(), criarPlayerAdmin()                ║
// ║                        → escolherinimigo(), jogoNormal()            ║
// ║                                                                     ║
// ╚═════════════════════════════════════════════════════════════════════╝


public class Main7 {

    // Globais do Random e Scanner
    private static final Random  rand  = new Random();
    private static final Scanner scan = new Scanner(System.in);

    // [MAIN]
    public static void main(String[] args) throws InterruptedException {
        Player player = new Player();
        Inimigo inimigo = new Inimigo();
        while(true) {
            System.out.println("1 - Jogar Normal");
            System.out.println("2 - Modo Admin");
            System.out.println("3 - Sair");

            int modo = lerOpcaoValida(1, 3);
            if (modo == 2) {
                if(admin()) {
                    System.out.println("✅ Você entrou no modo admin");
                    Thread.sleep(1500);
                } else {
                    System.out.println("❌ Senha incorreta");
                    return;
                }
                player = criarPlayerAdmin();
                escolherinimigo(player, inimigo);
                return;
            }
            else if (modo == 1) {
                jogoNormal(player);
                break;
            }
            else  {
                break;
            }
        }
    }

    // [ADMIN]

    public static boolean admin() throws InterruptedException {

        while (true) {

            System.out.println("Digite sua senha:");
            String senha = scan.next();

            if (senha.equals("admin12")) {

                System.out.println("✅ Senha correta!");
                return true;

            } else {

                System.out.println("❌ Senha incorreta! Tente novamente.");
                Thread.sleep(1000);

            }
        }
    }

    public static Player criarPlayerAdmin() throws InterruptedException {

        Player player = null;

        while (true) {
            limparTela();
            System.out.println("===== MODO ADMIN =====");
            System.out.println("Escolha sua classe");
            System.out.println(Cores.NEGRITO + Cores.VERMELHO_FORTE + "1) GUERREIRO" + Cores.RESET);
            System.out.println(Cores.NEGRITO + Cores.ROXO + "2) ASSASSINO" + Cores.RESET);
            System.out.println(Cores.NEGRITO + Cores.AZUL + "3) TANK" + Cores.RESET);

            int escolha = lerOpcaoValida(1, 3);
            switch (escolha) {
                case 1:
                    player = Player.criar(ClassesRpg.GUERREIRO);break;
                case 2:
                    player = Player.criar(ClassesRpg.ASSASSINO);break;
                case 3:
                    player = Player.criar(ClassesRpg.TANK);break;
                default:
                    System.out.println("Classe invalida!");
                    return Player.criar(ClassesRpg.GUERREIRO); // Define automaticamente como GUERREIRO
            }
            break;
        }
        limparTela();

        System.out.println("=== EDITAR STATUS ===");

        System.out.println("Vida: ");
        player.vida = lerOpcaoValida(1, 999);
        player.vidaMax = player.vida;

        System.out.println("Dano: ");
        player.dano = lerOpcaoValida(1, 999);

        System.out.println("Quantidade de Cura: ");
        player.quantidadeCura  = lerOpcaoValida(1, 999);
        limparTela();

        System.out.println(Cores.AZUL + "SUA CLASSE FOI CRIADA" + Cores.RESET);

        System.out.println(Cores.VERDE + "Vida: " + player.vida + Cores.RESET);
        System.out.println(Cores.VERMELHO + "Dano: " + player.dano + Cores.RESET);
        System.out.println(Cores.AZUL + "Cura: " + player.cura + Cores.RESET);
        limparTela();
        return player;
    }

    public static void escolherinimigo(Player player, Inimigo inimigo) throws InterruptedException{

        while (true) {
            System.out.println("Escolha o inimigo:");
            System.out.println(Cores.VERMELHO + "1) ESQUELETO" + Cores.RESET);
            System.out.println(Cores.VERMELHO +"2) GOBLIN" + Cores.RESET);
            System.out.println(Cores.VERMELHO +"3) DEMONIO" + Cores.RESET);
            System.out.println(Cores.VERMELHO +"4) CAVALEIRO NEGRO" + Cores.RESET);
            System.out.println(Cores.VERMELHO_FORTE +"5) REI DEMONIO" + Cores.RESET);
            System.out.println(Cores.NEGRITO +"6) SAIR" + Cores.RESET);

            int escolha = lerOpcaoValida(1, 6);
            switch (escolha) {
                case 1:
                    inimigo = Inimigo.criar(Inimigos.ESQUELETO);
                    executarFase(player, inimigo, escolha, escolha);
                    break;
                case 2:
                    inimigo = Inimigo.criar(Inimigos.GOBLIN);
                    executarFase(player, inimigo, escolha, escolha);
                    break;
                case 3:
                    inimigo = Inimigo.criar(Inimigos.DEMONIO);
                    executarFase(player, inimigo, escolha, escolha);
                    break;
                case 4:
                    inimigo = Inimigo.criar(Inimigos.CAVALEIRO_NEGRO);
                    executarFase(player, inimigo, escolha, escolha);
                    break;
                case 5:
                    inimigo = Inimigo.criar(Inimigos.REI_DEMONIO);
                    executarFase(player, inimigo, escolha, escolha);
                    break;
                default:
                    limparTela();
                    return;

            }
            limparTela();
        }
    }

    public static void jogoNormal(Player player) throws InterruptedException {
        player = escolhaClasse();
        Inimigo esqueleto      = Inimigo.criar(Inimigos.ESQUELETO);
        Inimigo goblin         = Inimigo.criar(Inimigos.GOBLIN);
        Inimigo demonio        = Inimigo.criar(Inimigos.DEMONIO);
        Inimigo cavaleiroNegro = Inimigo.criar(Inimigos.CAVALEIRO_NEGRO);
        Inimigo reiDemonio     = Inimigo.criar(Inimigos.REI_DEMONIO);

        digitar("🎮⚔️ " + Cores.NEGRITO + Cores.VERMELHO_FORTE + "BLOODIVINE "
                + Cores.AMARELO_FORTE + "INICIANDO... "
                + Cores.CIANO_FORTE + "Prepare-se para a batalha! 🎮⚔️" + Cores.RESET);
        Thread.sleep(1500);
        introducao();
        if (!executarFase(player, esqueleto,      1, 1)) return;
        pos_batalha_Esqueleto();
        if (!executarFase(player, goblin,         2, 2)) return;
        pos_batalha_Goblin();
        if (!executarFase(player, demonio,        3, 3)) return;
        pos_batalha_Demonio();
        if (!executarFase(player, cavaleiroNegro, 4, 4)) return;
        pos_batalha_Cavaleiro_Negro();
        if (!executarFase(player, reiDemonio,     5, 5)) return;

        limparTela();
        finalDoJogo();

        scan.close();
    }


    // [UTILITÁRIOS]


    // [UTILITÁRIOS > Console]

    public static void digitar(String texto) throws InterruptedException {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            Thread.sleep(20);
        }
        System.out.println();
    }

    public static void limparTela() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public static void limparTelaDevagar() throws InterruptedException {
        for (int i = 0; i < 50; i++) {System.out.println(); Thread.sleep(40);}
    }

    public static int lerOpcaoValida(int min, int max) throws InterruptedException {
        while (true) {
            if (!scan.hasNextInt()) {

                digitar("❌ Opção inválida. Digite um número de " + min + " a " + max + ".");
                scan.next();
                Thread.sleep(1000);

                continue;
            }
            int opcao = scan.nextInt();
            if (opcao >= min && opcao <= max) return opcao;

            digitar("❌ Opção inválida. Digite um número de " + min + " a " + max + ".");
            Thread.sleep(1000);

        }
    }

    // [UTILITÁRIOS > Cores]

    public static class Cores {
        public static final String VERDE            = "\033[32m";
        public static final String VERDE_BRILHANTE  = "\033[1;32m";
        public static final String VERMELHO         = "\033[31m";
        public static final String AZUL             = "\033[34m";
        public static final String AZUL_BRILHANTE   = "\033[1;34m";
        public static final String ROXO             = "\033[35m";
        public static final String AMARELO_FORTE    = "\033[1;33m";
        public static final String CIANO_FORTE      = "\033[1;36m";
        public static final String VERMELHO_FORTE   = "\033[1;31m";
        public static final String NEGRITO          = "\033[1m";
        public static final String RESET            = "\033[0m";
    }

    // [CLASSES RPG]
    public static class ClassesRpg {
        public static final String GUERREIRO = "GUERREIRO";
        public static final String ASSASSINO = "ASSASSINO";
        public static final String TANK      = "TANK";

        // Separei a string colorida da string normal para poder usar EX: Player.criar(ClassesRpg.GUERREIRO)
        // Esse método aqui mostra a string colorida
        public static String formatado(String classe) {
            switch (classe) {
                case GUERREIRO: return Cores.NEGRITO + Cores.VERMELHO_FORTE + "GUERREIRO" + Cores.RESET;
                case ASSASSINO: return Cores.NEGRITO + Cores.ROXO           + "ASSASSINO" + Cores.RESET;
                case TANK:      return Cores.NEGRITO + Cores.AZUL           + "TANK"      + Cores.RESET;
                default:        return classe;
            }
        }
    }


    // [INIMIGOS]

    // [INIMIGOS > Constantes]

    public static class Inimigos {
        public static final String ESQUELETO       = "ESQUELETO";
        public static final String GOBLIN          = "GOBLIN";
        public static final String DEMONIO         = "DEMÔNIO";
        public static final String CAVALEIRO_NEGRO = "CAVALEIRO-NEGRO";
        public static final String REI_DEMONIO     = "REI-DEMÔNIO";
    }

    // [INIMIGOS > Modelo]

    public static class Inimigo {
        int vida;
        int dano;
        String nome;

        public void atacar(Player player) {
            player.receberDano(dano);
        }

        public static Inimigo criar(String nome) {
            Inimigo ini = new Inimigo();
            ini.nome = nome;

            switch (nome) {
                case Inimigos.ESQUELETO:
                    ini.vida = 50;  ini.dano = rand.nextInt(6)  + 5;  break;
                case Inimigos.GOBLIN:
                    ini.vida = 80;  ini.dano = rand.nextInt(10) + 8;  break;
                case Inimigos.DEMONIO:
                    ini.vida = 100; ini.dano = rand.nextInt(15) + 12; break;
                case Inimigos.CAVALEIRO_NEGRO:
                    ini.vida = 130; ini.dano = rand.nextInt(16) + 15; break;
                case Inimigos.REI_DEMONIO:
                    ini.vida = 220; ini.dano = rand.nextInt(20) + 20; break;
                default:
                    throw new IllegalArgumentException("Inimigo desconhecido: " + nome);
            }
            return ini;
        }
    }

    // [PLAYER]


    // [PLAYER > Modelo]

    public static class Player {
        int vida;
        int nivel2 = 1;
        int vidaMax;
        int dano;
        int cura;
        int quantidadeCura;
        int iniciativa;
        String classe;

        public void receberDano(int danoRecebido) {
            vida -= danoRecebido;
        }

        public void curar() {
            if (quantidadeCura <= 0) {
                System.out.println(Cores.AMARELO_FORTE + "❌ Sem curas disponíveis!" + Cores.RESET);
                return;
            }
            vida = Math.min(vida + cura, vidaMax);
            quantidadeCura--;
            System.out.println("✨ Curado! Vida: " + Cores.VERDE + vida + "/" + vidaMax + Cores.RESET);
        }

        public void resetarVida() {
            vida = vidaMax;
        }

        public void aplicarBuff(int tipoBuff, int nivel) {
            int[] bonus = calcularBonusPorNivel(nivel);
            switch (tipoBuff) {
                case 1:
                    vidaMax += bonus[0];
                    vida = vidaMax;
                    System.out.printf(Cores.VERMELHO      + "❤️  +%d de vida máxima! " + Cores.RESET + "Vida: %d/%d%n",  bonus[0], vida, vidaMax);
                    break;
                case 2:
                    dano += bonus[1];
                    System.out.printf(Cores.AMARELO_FORTE + "⚔️  +%d de dano!        " + Cores.RESET + "Dano atual: %d%n", bonus[1], dano);
                    break;
                case 3:
                    quantidadeCura += bonus[2];
                    System.out.printf(Cores.VERDE         + "🧪 +%d de cura!         " + Cores.RESET + "Curas: %d%n",      bonus[2], quantidadeCura);
                    break;
            }
            nivel2 = nivel;
        }

        // Retorna um vetor de int [vidaBonus, danoBonus, curaBonus] diferente em cada nível
        private int[] calcularBonusPorNivel(int nivel) {
            switch (nivel) {
                case 1: return new int[]{ rand.nextInt(10) + 15, rand.nextInt(4) + 4,  1 };
                case 2: return new int[]{ rand.nextInt(11) + 25, rand.nextInt(6) + 7,  1 };
                case 3: return new int[]{ rand.nextInt(6)  + 35, rand.nextInt(5) + 10, 1 };
                case 4: return new int[]{ rand.nextInt(16) + 30, rand.nextInt(5) + 13, 1 };
                case 5: return new int[]{ rand.nextInt(8)  + 43, rand.nextInt(5) + 16, 2 };
                default: return new int[]{ 0, 0, 0 };
            }
        }

        public static Player criar(String classe) {
            Player p = new Player();
            p.classe = classe;
            switch (classe) {
                case ClassesRpg.GUERREIRO:
                    p.vida = p.vidaMax = 100; p.dano = 20; p.cura = 30; p.quantidadeCura = 4; p.iniciativa = 10; break;
                case ClassesRpg.ASSASSINO:
                    p.vida = p.vidaMax = 90;  p.dano = 25; p.cura = 25; p.quantidadeCura = 3; p.iniciativa = 15; break;
                case ClassesRpg.TANK:
                    p.vida = p.vidaMax = 150; p.dano = 17; p.cura = 40; p.quantidadeCura = 5; p.iniciativa = 5;  break;
            }
            return p;
        }
    }

    // [PLAYER > Criação]

    // esse método carrega o título, e lida com a escolha de classe retornando um Player
    public static Player escolhaClasse() throws InterruptedException {
        digitar("Bem-Vindo à história de");
        System.out.println();
        Thread.sleep(1000);
        imprimirTituloBlooDivine();
        Thread.sleep(1500);
        limparTelaDevagar();

        digitar("Escolha sua classe no RPG de " + Cores.VERMELHO_FORTE + "BLOODIVINE" + Cores.RESET);
        System.out.println("1) ⚔️  " + ClassesRpg.formatado(ClassesRpg.GUERREIRO) + " — 100HP | 20ATK | 4 curas | Iniciativa média");
        System.out.println("2) 🗡️  " + ClassesRpg.formatado(ClassesRpg.ASSASSINO) + " — 90HP  | 25ATK | 3 curas | Alta iniciativa");
        System.out.println("3) 🛡️  " + ClassesRpg.formatado(ClassesRpg.TANK)      + " — 150HP | 17ATK | 5 curas | Baixa iniciativa");
        System.out.print("Opção: ");

        String classe = opcaoParaClasse(lerOpcaoValida(1, 3));
        digitar("Sua classe: " + ClassesRpg.formatado(classe));
        Thread.sleep(1000);
        limparTela();
        return Player.criar(classe);
    }

    private static String opcaoParaClasse(int opcao) {
        switch (opcao) {
            case 1:  return ClassesRpg.GUERREIRO;
            case 2:  return ClassesRpg.ASSASSINO;
            default: return ClassesRpg.TANK;
        }
    }

    // [PLAYER > Buffs]

    // extraindo a lógica do buff da classe do player
    public static void escolherEAplicarBuff(Player player, int nivelBuff) throws InterruptedException {
        limparTela();
        digitar(Cores.VERDE + "Parabéns! Buff de Nível " + nivelBuff + " disponível!" + Cores.RESET);
        Thread.sleep(1500);
        limparTela();

        digitar("Escolha o buff (Nível " + nivelBuff + "):");
        System.out.println("1) ❤️  Vida Máxima — " + Cores.VERMELHO        + "Aumenta o HP total."          + Cores.RESET);
        System.out.println("2) ⚔️  Dano        — " + Cores.AZUL_BRILHANTE  + "Aumenta o dano por ataque."   + Cores.RESET);
        System.out.println("3) 🧪 Curas       — " + Cores.VERDE_BRILHANTE + "Recebe mais poções de cura."  + Cores.RESET);
        System.out.print("Opção: ");

        player.aplicarBuff(lerOpcaoValida(1, 3), nivelBuff);
        Thread.sleep(1000);
        limparTelaDevagar();
    }

    // [COMBATE]

    //Variáveis globais do combate
    private static final int CHANCE_CRITICO_PERCENT = 30; //%(porcentagem) de chance de acertar um crítico
    private static final int CRITICO_BONUS_MIN      = 20; //%(porcentagem) mínima de dano extra no crítico
    private static final int CRITICO_BONUS_MAX      = 80; //%(porcentagem) máxima de dano extra no crítico

    // [COMBATE > Iniciativa]

    // Rola o "D20" e retorna true se o player age primeiro e false se o inimigo age primeiro
    private static boolean playerTemIniciativa(Player player) {
        int rolagemPlayer  = rand.nextInt(20) + player.iniciativa;
        int rolagemInimigo = rand.nextInt(20);
        return rolagemPlayer >= rolagemInimigo;
    }

    // [COMBATE > Crítico]

    // Chance de 30%. Bônus de 20% a 80% do dano base.
    private static int calcularDanoCritico(int danoBase) throws InterruptedException {
        boolean FoiCritico = rand.nextInt(100) < CHANCE_CRITICO_PERCENT;
        if (!FoiCritico) return danoBase;

        int bonusPercent = rand.nextInt(CRITICO_BONUS_MAX - CRITICO_BONUS_MIN) + CRITICO_BONUS_MIN;
        int danoFinal    = danoBase + (danoBase * bonusPercent / 100);
        digitar(Cores.AMARELO_FORTE + "💥 CRÍTICO! (+" + bonusPercent + "%) " + danoBase + " → " + danoFinal + Cores.RESET);
        return danoFinal;
    }

    // [COMBATE > Ataques]

    // Aqui aplico o dano que já vem calculado e se foi crítico ou não
    private static void aplicarAtaquePlayer(Player player, Inimigo inimigo) throws InterruptedException {
        int dano = calcularDanoCritico(player.dano);
        inimigo.vida -= dano;
        digitar("⚔️ Você atacou " + Cores.VERMELHO + inimigo.nome + Cores.RESET
                + "! Dano: " + Cores.AZUL + dano + Cores.RESET);
    }

    // podeDarCritico = false quando o player está se curando
    // Inimigo não pode acertar um critico durante a cura
    private static void aplicarAtaqueInimigo(Inimigo inimigo, Player player, boolean podeDarCritico) throws InterruptedException {
        int dano = 0;
        if (podeDarCritico)
            dano = calcularDanoCritico(inimigo.dano);
        else
            dano = inimigo.dano;
        player.receberDano(dano);
        digitar(Cores.VERMELHO + "🩸 " + inimigo.nome + " causou " + dano + " de dano!" + Cores.RESET);
    }

    // [COMBATE > Turnos]

    // Turno de ataque: rola iniciativa e resolve a ordem das ações.
    private static void turnoAtaque(Player player, Inimigo inimigo) throws InterruptedException {
        if (playerTemIniciativa(player)) {
            digitar(Cores.CIANO_FORTE + "⚡ Você age primeiro!" + Cores.RESET);
            aplicarAtaquePlayer(player, inimigo);
            if (inimigo.vida > 0) {
                digitar(Cores.VERMELHO + inimigo.nome + " contra-ataca!" + Cores.RESET);
                aplicarAtaqueInimigo(inimigo, player, true); // crítico permitido
            }
        } else {
            digitar(Cores.VERMELHO + "⚡ " + inimigo.nome + " age primeiro!" + Cores.RESET);
            aplicarAtaqueInimigo(inimigo, player, true); // crítico permitido
            if (player.vida > 0) aplicarAtaquePlayer(player, inimigo);
        }
    }

    // Turno de cura: inimigo pode atacar de volta, mas SEM chance de crítico.
    private static void turnoCura(Player player, Inimigo inimigo) throws InterruptedException {
        int inimigoAcertouAtaque = rand.nextInt(0,3);
        player.curar();
        if (inimigoAcertouAtaque == 2){
            digitar("O " + inimigo.nome + " ataca durante sua cura!");
            aplicarAtaqueInimigo(inimigo, player, false); // crítico BLOQUEADO
        }
        else {
            digitar("O " + inimigo.nome + " errou o ataque durante a cura!");
        }
    }

    // [COMBATE > Fluxo]

    public static boolean ExecutarCombate(Player player, Inimigo inimigo) throws InterruptedException {
        int round         = 1;
        int danoAcumulado = 0;

        digitar("\nApareceu um " + Cores.VERMELHO + inimigo.nome + Cores.RESET + "!!");

        while (player.vida > 0 && inimigo.vida > 0) {
            int vidaAntes = player.vida;
            mostrarHud(inimigo, player, round, player.nivel2);

            int escolha = lerOpcaoValida(1, 3);

            // Status não consome round
            if (escolha == 3) {
                mostrarStatus(player, danoAcumulado);
                limparTelaDevagar();
                continue;
            }

            if (escolha == 1) turnoAtaque(player, inimigo);
            else              turnoCura(player, inimigo);

            danoAcumulado += Math.max(0, vidaAntes - player.vida);
            Thread.sleep(1500);
            round++;
            limparTela();
        }

        if (player.vida <= 0) {
            digitar("☠️ Você morreu...");
            return false;
        }
        digitar("🎖️ Você venceu " + Cores.VERMELHO + inimigo.nome + Cores.RESET + "!");
        Thread.sleep(1500);
        return true;
    }

    // Encapsula combate + reset de vida + escolha de buff em uma só fase
    private static boolean executarFase(Player player, Inimigo inimigo, int nivelBuff, int salaAtual) throws InterruptedException {
        mostrarSala(salaAtual);
        if (!ExecutarCombate(player, inimigo)) return false;
        player.resetarVida();
        escolherEAplicarBuff(player, nivelBuff);
        return true;
    }

    // [GRÁFICOS]


    // [GRÁFICOS > HUD]

    public static void mostrarHud(Inimigo inimigo, Player player, int round, int nivel2) {
        System.out.println("— Round " + round + " ─────────────────── Nivel "+ nivel2 + " ────");
        System.out.println(Cores.VERDE    + "❤️ Sua Vida: " + player.vida + "/" + player.vidaMax + Cores.RESET);
        System.out.println(Cores.VERMELHO + "👾 " + inimigo.nome + ": " + inimigo.vida + Cores.RESET);
        System.out.println(Cores.AZUL     + "📦 Curas disponíveis: " + player.quantidadeCura + Cores.RESET);
        System.out.println("──────────────────────────────────────────");
        System.out.println("1) ⚔️  Atacar   2) 🧪 Curar   3) 📊 Status");
        System.out.print("Opção: ");
    }

    public static void mostrarStatus(Player player, int danoAcumulado) throws InterruptedException {
        System.out.println("═══════════ 📊 Status ═══════════");
        System.out.println("Classe:              " + ClassesRpg.formatado(player.classe));
        System.out.println("Vida:                " + Cores.VERDE          + player.vida + "/" + player.vidaMax + Cores.RESET);
        System.out.println("Dano base:           " + Cores.VERMELHO_FORTE + player.dano                        + Cores.RESET);
        System.out.println("Curas restantes:     " + Cores.AZUL           + player.quantidadeCura              + Cores.RESET);
        System.out.println("Dano total recebido: " + Cores.VERMELHO       + "-" + danoAcumulado                + Cores.RESET);
        System.out.println("═════════════════════════════════");
        Thread.sleep(1500);
    }

    // [GRÁFICOS > Arte ASCII]

    public static void imprimirTituloBlooDivine() throws InterruptedException {

        String[] titulo_BlooDivine = {
                "██████╗ ██╗      ██████╗  ██████╗ ██████╗ ██╗██╗   ██╗██╗███╗   ██╗███████╗",
                "██╔══██╗██║     ██╔═══██╗██╔═══██╗██╔══██╗██║██║   ██║██║████╗  ██║██╔════╝",
                "██████╔╝██║     ██║   ██║██║   ██║██║  ██║██║██║   ██║██║██╔██╗ ██║█████╗  ",
                "██╔══██╗██║     ██║   ██║██║   ██║██║  ██║██║╚██╗ ██╔╝██║██║╚██╗██║██╔══╝  ",
                "██████╔╝███████╗╚██████╔╝╚██████╔╝██████╔╝██║ ╚████╔╝ ██║██║ ╚████║███████╗",
                "╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝  ╚═╝╚═╝  ╚═══╝╚══════╝"
        };

        int[][] cores = {
                {255, 0, 0},
                {220, 0, 20},
                {190, 0, 40},
                {160, 0, 60},
                {130, 0, 80},
                {100, 0, 100}
        };

        for (int i = 0; i < titulo_BlooDivine.length; i++) {
            int r = cores[i][0];
            int g = cores[i][1];
            int b = cores[i][2];

            // Define a cor da linha
            System.out.print("\u001B[38;2;" + r + ";" + g + ";" + b + "m");

            // Percorre cada caractere da linha para o efeito de digitação
            String linha = titulo_BlooDivine[i];
            for (int j = 0; j < linha.length(); j++) {
                System.out.print(linha.charAt(j));
                // Velocidade mais rápida (5ms) para não demorar demais,
                // já que o título tem muitos caracteres
                Thread.sleep(5);
            }

            System.out.println(); // Pula para a próxima linha do desenho
        }

        System.out.println("\u001B[0m"); // Reseta a cor
    }

    // [GRÁFICOS > Salas]

    public static void duasLinhas() {
        limparTela();
        System.out.println("* * * * * * * * * * * * * * * ");
        System.out.println("* * * * * * * * * * * * * * * ");
    }

    public static void apagar() {
        for (int j = 0; j < 30; j++) {
            System.out.print("\b");
        }
    }

    public static void entrarSala(int sala) throws InterruptedException {
        System.out.println();
        limparTela();
        System.out.println("Entrando na sala " + sala + "...");
        Thread.sleep(1000);
    }


    public static void mostrarSala(int sala) throws InterruptedException {
        switch (sala) {
            case 1 -> {
                duasLinhas();
                for (int i = 0; i < 3; i++) {
                    System.out.print("*" + "\u001B[31m" + " 1 " + "\u001B[0m" + "* * 2 * * 3 * * 4 * * 5 * ");
                    Thread.sleep(500);
                    apagar();
                    System.out.print("*" + "\u001B[31m" + "  "+" " + "\u001B[0m" + "* * 2 * * 3 * * 4 * * 5 * ");
                    Thread.sleep(500);
                    apagar();
                }
                entrarSala(1);

                pre_batalha_esqueleto();
                limparTelaDevagar();
            }
            case 2 -> {
                duasLinhas();
                for (int i = 0; i < 3; i++) {
                    System.out.print("* 1 * *" + "\u001B[31m" + "   "+"\u001B[0m"+"* * 3 * * 4 * * 5 * ");
                    Thread.sleep(450);
                    apagar();
                    System.out.print("* 1 * *" + "\u001B[31m" + " 2 "+"\u001B[0m"+"* * 3 * * 4 * * 5 * ");
                    Thread.sleep(450);
                    apagar();
                }
                entrarSala(2);

                pre_batalha_goblin();
                limparTelaDevagar();
            }
            case 3 -> {
                duasLinhas();
                for (int i = 0; i < 4; i++) {
                    System.out.print("* 1 * * 2 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* * 4 * * 5 * ");
                    Thread.sleep(450);
                    apagar();
                    System.out.print("* 1 * * 2 * *"+"\u001B[31m"+" 3 "+"\u001B[0m"+"* * 4 * * 5 * ");
                    Thread.sleep(450);
                    apagar();
                }
                entrarSala(3);

                pre_batalha_Demonio();
                limparTelaDevagar();
            }
            case 4 -> {
                duasLinhas();
                for (int i = 0; i < 4; i++) {
                    System.out.print("* 1 * * 2 * * 3 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* * 5 * ");
                    Thread.sleep(450);
                    apagar();
                    System.out.print("* 1 * * 2 * * 3 * *"+"\u001B[31m"+" 4 "+"\u001B[0m"+"* * 5 * ");
                    Thread.sleep(450);
                    apagar();
                }
                entrarSala(4);

                pre_batalha_Cavaleiro_Negro();
                limparTelaDevagar();
            }
            case 5 -> {
                duasLinhas();
                for (int i = 0; i < 4; i++) {
                    System.out.print("* 1 * * 2 * * 3 * * 4 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* ");
                    Thread.sleep(450);
                    apagar();
                    System.out.print("* 1 * * 2 * * 3 * * 4 * *"+"\u001B[31m"+" 5 "+"\u001B[0m"+"* ");
                    Thread.sleep(450);
                    apagar();}
                entrarSala(5);


                pre_batalha_rei_demonio();
                limparTelaDevagar();
            }
            default -> { System.out.println("ERRO! Número de sala inválido, apenas de 1 até 5");}
        }
    }

    // [NARRADOR]

    //[NARRADOR > Introdução]
    public static void introducao() throws InterruptedException {
        limparTela();
        digitar("Há muitos séculos...");
        Thread.sleep(2500);
        digitar("os Cavaleiros Templários lutavam em nome da Igreja contra as forças do mal.");
        Thread.sleep(1000);
        digitar("Durante as guerras, a" + Cores.AMARELO_FORTE+ " Igreja do Calvário " + Cores.RESET + "foi destruída e o hoje permanece abandonada...");
        Thread.sleep(2000);
        digitar("Para purificar o templo, um nobre" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "adentra as escadarias da igreja...");
        Thread.sleep(2000);
    }

    //[NARRADOR > Pre-Batalha]

    public static void pre_batalha_esqueleto() throws InterruptedException {
        limparTela();
        digitar("O " + Cores.AZUL + "Cavaleiro" + Cores.RESET + ", imerso na escuridão...");
        digitar("Acende uma tocha revelando ossos espalhados pela sala");
        Thread.sleep(1000);
        digitar("Ao som das gotas das paredes úmidas...                             ");
        digitar("Os ossos da caverna começam a se juntar...                         ");
        digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.ESQUELETO + Cores.RESET + "!");
        Thread.sleep(2200);
    }

    public static void pre_batalha_goblin() throws InterruptedException {
        digitar("De uma silhueta escura se avista uma pele" + Cores.VERDE + " esverdeada..." + Cores.RESET);
        Thread.sleep(1000);
        digitar("A figura ronda o " + Cores.AZUL + "Cavaleiro " + Cores.RESET + "pela escuridão esperando a melhor oportunidade de " + Cores.VERMELHO_FORTE + "ATACAR!" + Cores.RESET);
        digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.GOBLIN + Cores.RESET + "!");
        Thread.sleep(2200);
    }

    public static void pre_batalha_Demonio() throws InterruptedException {
        limparTela();
        digitar("As paredes de pedra mascadas com " + Cores.VERMELHO_FORTE + "sangue " + Cores.RESET + "revelam um convecionário");
        Thread.sleep(2000);
        digitar("No teto, um vulto, algo está se mexendo e muito rápido...");
        Thread.sleep(1000);
        digitar("Uma criatura de "+ Cores.NEGRITO + "asas negras " + Cores.RESET + "desce em sua direção                ");
        Thread.sleep(1000);
        digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.DEMONIO + Cores.RESET + "!");
        Thread.sleep(2200);
    }

    public static void pre_batalha_Cavaleiro_Negro() throws InterruptedException {
        limparTela();
        digitar("Ao centro de um grande salão iluminado por velas...");
        Thread.sleep(1500);
        digitar("Uma figura está posta ajoelhada em oração para uma grande imagem de" + Cores.AMARELO_FORTE + " Cristo" + Cores.RESET);
        Thread.sleep(1000);
        digitar("A figura imponente se ergue, vestida com uma" + Cores.NEGRITO + " armadura negra reluzente..." + Cores.RESET);
        Thread.sleep(1500);
        digitar("E com um único golpe");
        digitar(Cores.VERMELHO + "Destroi a imagem " + Cores.RESET + "enfurecendo o" + Cores.AZUL + " Cavaleiro" + Cores.RESET);
        Thread.sleep(2000);
        digitar("O inimigo é o " + Cores.VERMELHO + Inimigos.CAVALEIRO_NEGRO + Cores.RESET + "!");
        Thread.sleep(2200);
    }

    public static void pre_batalha_rei_demonio() throws InterruptedException {
        limparTela();
        digitar("Em instantes");
        Thread.sleep(1000);
        digitar("A escuridão que cobria a igreja...");
        Thread.sleep(1500);
        digitar(Cores.VERMELHO + "Foi tomada por um fogo" + Cores.RESET + " que envolvia um" + Cores.NEGRITO  + " Grande Trono Do Castelo Sombrio" + Cores.RESET);
        Thread.sleep(2000);
        digitar("De lá, o " + Cores.VERMELHO + "Rei Demônio" + Cores.RESET + " aguarda sua chegada...");
        Thread.sleep(2200);
        digitar("O inimigo é o " + Cores.VERMELHO + Inimigos.REI_DEMONIO + Cores.RESET + "!");
        Thread.sleep(600);
        digitar("Se prepare " + Cores.AMARELO_FORTE + "Cavaleiro" + Cores.RESET + "!");
        digitar("A batalha final se aproxima... Este é o desafio supremo o desafio supremo!  ⚔️ 👑");
        Thread.sleep(2200);
    }


    //[NARRADOR > Pos-Batalha]

    public static void pos_batalha_Esqueleto() throws InterruptedException {
        limparTela();
        digitar("O " + Cores.VERMELHO + "ESQUELETO foi derrotado" + Cores.RESET);
        Thread.sleep(1500);
        digitar("Mas o que o " + Cores.AZUL +"Cavaleiro " + Cores.AZUL + "havia enfrentado de fato?");
        digitar("Seria essa criatura fruto de sua " + Cores.NEGRITO + "imaginação..." + Cores.RESET);
        Thread.sleep(1500);
        digitar(Cores.NEGRITO + "Ele temia que não!" + Cores.RESET);
        Thread.sleep(2500);
    }

    public static void pos_batalha_Goblin() throws InterruptedException {
        limparTela();
        digitar("O cravejar da lâmina do" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "ecoa como um último som antes do silencio...");
        Thread.sleep(2000);
        digitar("aquele silêncio deixava claro...");
        Thread.sleep(1000);
        digitar("A " + Cores.VERMELHO_FORTE + "Igreja do Calvário " + Cores.RESET + "era um templo " + Cores.NEGRITO + "das trevas " + Cores.RESET);
        Thread.sleep(2200);
    }

    public static void pos_batalha_Demonio() throws InterruptedException {
        limparTela();
        digitar("O" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "sentia um aperto em seu coração");
        Thread.sleep(1500);
        digitar("Como poderam manchar o nome da" + Cores.AMARELO_FORTE + " Igreja" + Cores.RESET + "?" + " Ele se perguntava");
        Thread.sleep(1500);
        digitar("O" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "havia entendido o seu propósito...");
        Thread.sleep(1500);
        digitar(Cores.NEGRITO + "Limpar o legado da " + Cores.RESET + Cores.AMARELO_FORTE + "Igreja do Calvário " + Cores.RESET
                + Cores.NEGRITO + "\n e derrotar todas as criaturas profanas no templo!" + Cores.RESET);
        Thread.sleep(3000);
    }

    public static void pos_batalha_Cavaleiro_Negro() throws InterruptedException {
        limparTela();
        digitar("O" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "atravessou a câmara em silêncio");
        Thread.sleep(1500);
        digitar("e continuou avançando pelas profundezas da antiga igreja...");
        Thread.sleep(1500);
        digitar("Mal sabia ele que um grande desafio o" + Cores.VERMELHO_FORTE + " aguardava na próxima sala" + Cores.RESET);
        Thread.sleep(2500);
    }

    public static void finalDoJogo()  throws InterruptedException{
        limparTela();
        digitar(Cores.AMARELO_FORTE + "O Rei Demônio foi derrotado!" + Cores.RESET);
        digitar(Cores.AMARELO_FORTE + "🏆 O mundo está a salvo!" + Cores.RESET);
        Thread.sleep(1500);
        digitar("O" + Cores.AZUL + " Cavaleiro " + Cores.RESET + "havia cumprido seu destino.");
        Thread.sleep(1500);
        digitar("Agora ele pode descansar em paz...");
        Thread.sleep(1500);
        limparTela();
        imprimirTituloBlooDivine();
        digitar(Cores.CIANO_FORTE   + "★BLOODIVINE COMPLETO! Parabéns, herói! ★" + Cores.RESET);

    }
}