import SwiftUI

@main
struct iOSApp: App {

    init() {
        // DiHelperKt.initKoin()
        DiHelperKt.doInitKoin()
    }

	var body: some Scene {
		WindowGroup {
			ZStack {
                Color.white.ignoresSafeArea(.all) // status bar color
                ContentView()
            }.preferredColorScheme(.light)
		}
	}
}