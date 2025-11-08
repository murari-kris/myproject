let localStream;

async function initMic() {
  try {
   
    localStream = await navigator.mediaDevices.getUserMedia({ audio: true });

    console.log("✅ Microphone access granted");

   
    const audio = document.createElement("audio");
    audio.srcObject = localStream;
    audio.autoplay = true;
    audio.muted = true;  
    document.body.appendChild(audio);

  } catch (error) {
    console.error("❌ Error accessing microphone:", error);
    alert("Please allow microphone access in your browser settings.");
  }
}
