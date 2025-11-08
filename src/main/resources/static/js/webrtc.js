let peerConnection;
let ws;

function createPeer() {
  peerConnection = new RTCPeerConnection();

  // Add ICE candidates
  peerConnection.onicecandidate = (event) => {
    if (event.candidate) {
      ws.send(JSON.stringify({ type: "candidate", candidate: event.candidate }));
    }
  };

  // Play remote audio
  peerConnection.ontrack = (event) => {
    const audio = document.createElement("audio");
    audio.srcObject = event.streams[0];
    audio.autoplay = true;
    document.body.appendChild(audio);
  };
}

async function call() {
  // Connect WebSocket (use global ws, not a new const)
  ws = new WebSocket(`ws://${window.location.host}/signal`);

  ws.onopen = async () => {
    console.log("✅ WebSocket connected");
    createPeer();

    // Add local mic stream
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    stream.getTracks().forEach((track) => peerConnection.addTrack(track, stream));

    // Create and send offer
    const offer = await peerConnection.createOffer();
    await peerConnection.setLocalDescription(offer);

    ws.send(JSON.stringify({ type: "offer", sdp: offer.sdp }));
  };

  // Handle signaling messages
  ws.onmessage = async (msg) => {
    const data = JSON.parse(msg.data);

    if (data.type === "answer") {
      await peerConnection.setRemoteDescription({ type: "answer", sdp: data.sdp });
    } else if (data.type === "candidate") {
      await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
    }
  };
}
