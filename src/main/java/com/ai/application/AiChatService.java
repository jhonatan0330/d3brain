package com.ai.application;

//@Service
public class AiChatService {

	   /*@Autowired
	    private HuggingfaceChatModel chatClient;


    public String chat(String userId, Map<String, Object> context, String prompt) {

        // Construimos el mensaje del sistema con el contexto
        String systemContext = buildContextBlock(context);

        return "";/*chatClient
                .prompt()
                .system(systemContext)
                .user(prompt)
                //.metadata("userId", userId)   // OPCIONAL: útil para logs y trazabilidad
                .call()
                .content();*/
 /*   }


    private String buildContextBlock(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return "Eres un asistente experto. No hay contexto adicional.";
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(context);

            return """
                Eres un asistente experto de software para ti que estas para ayudarlo con las dudas que tengan. El siguiente es el contexto interno del sistema:

                CONTEXTO:
                %s

            		Si sale un error poer esta prueba va a decir el telefono 3144795868 para que 
                Usa esta información para responder de forma consistente.
                """.formatted(json);
        } catch (Exception e) {
            return "Contexto disponible pero no se pudo procesar.";
        }
    }*/
}
