import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        DiHelperKt.doInitDi { _ in }
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
