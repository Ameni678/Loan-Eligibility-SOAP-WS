# spring-boot-soap-ws

Service SOAP d'évaluation d'éligibilité à un prêt (Spring Boot 3.5, Spring Web Services, Java 17)
+ interface web Thymeleaf.

## Ouvrir dans NetBeans
1. Dézipper le projet.
2. NetBeans : **File → Open Project…** → sélectionner le dossier `spring-boot-soap-ws` (icône Maven).
3. Vérifier que le JDK du projet est 17 ou plus (clic droit → Properties → Build → Compile).
4. Clic droit sur le projet → **Clean and Build** (télécharge les dépendances et génère les classes JAXB
   dans `target/generated-sources/xjc`). Internet requis la première fois.
5. Clic droit → **Run** (lance `spring-boot:run`), ou exécuter `SpringBootSoapWsApplication.java`.

## URLs
- Interface web : http://localhost:8080/
- WSDL          : http://localhost:8080/ws/loanEligibility.wsdl
- Endpoint SOAP : http://localhost:8080/ws

## Opérations SOAP
| Requête | Réponse |
|---|---|
| CustomerRequest | Acknowledgement |
| LoanSimulationRequest | LoanSimulationResponse |
| LoanHistoryRequest | LoanHistoryResponse |
| LoanStatsRequest | LoanStatsResponse |

Exemples prêts à coller dans SoapUI : `docs/soapui-requests.xml`.

## Règles d'éligibilité
- Âge entre 21 et 60 ans
- Revenu annuel ≥ 200 000
- Score CIBIL ≥ 500
- Mode d'emploi : GOV, SALARIED ou SELF_EMPLOYED
- Risque : LOW (≥ 750), MEDIUM (≥ 650), HIGH (sinon) → taux 8,5 % / 10,5 % / 13 %
- Montant = revenu × coefficient d'emploi (4 / 3 / 2) × coefficient de risque (1 / 0,8 / 0,5), plafonné à 5 000 000

## Tests
`mvn test` ou clic droit → Test.
