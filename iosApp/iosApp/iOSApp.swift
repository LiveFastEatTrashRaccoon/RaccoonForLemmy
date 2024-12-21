import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        DiHelperKt.initDi()
    }
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                // status bar color
                Color.black.ignoresSafeArea(.all)
                ContentView()
            }
            .preferredColorScheme(.dark)
        }
    }
}
