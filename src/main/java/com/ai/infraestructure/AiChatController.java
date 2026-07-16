package com.ai.infraestructure;

//@RestController
//@RequestMapping("/ai")
public class AiChatController {
	/*
	 * private final AiChatService aiChatService;
	 * 
	 * public AiChatController(AiChatService aiChatService) { this.aiChatService =
	 * aiChatService; }
	 * 
	 * @PostMapping("/chat") public ResponseEntity<String> chat(@RequestBody
	 * ChatRequest request) { if (request.prompt() == null ||
	 * request.prompt().trim().isEmpty()) { return
	 * ResponseEntity.badRequest().body("El campo 'prompt' no puede estar vacío.");
	 * }
	 * 
	 * String response = aiChatService.chat( request.userId(), request.context(),
	 * request.prompt() );
	 * 
	 * return ResponseEntity.ok(response); }
	 * 
	 * @PostMapping("/chat2") public ResponseEntity<?> chat2(@RequestBody String
	 * raw) { System.out.println("RAW BODY: " + raw); return
	 * ResponseEntity.ok("OK"); }
	 */
	/*
	 * private final HuggingfaceChatModel chatModel;
	 * 
	 * @Autowired public AiChatController(HuggingfaceChatModel chatModel) {
	 * this.chatModel = chatModel; }
	 * 
	 * @GetMapping("/ai/generate") public Map generate(@RequestParam(value =
	 * "message", defaultValue = "Tell me a joke") String message) { return
	 * Map.of("generation", this.chatModel.call(message)); }
	 */
}