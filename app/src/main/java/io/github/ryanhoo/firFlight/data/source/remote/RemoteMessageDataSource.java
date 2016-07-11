package io.github.ryanhoo.firFlight.data.source.remote;

import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.source.MessageContract;
import io.github.ryanhoo.firFlight.data.source.remote.api.RESTFulApiService;
import io.github.ryanhoo.firFlight.network.MultiPageResponse;
import rx.Observable;
import rx.functions.Func1;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/2/16
 * Time: 9:46 PM
 * Desc: RemoteMessageDataSource
 */
public class RemoteMessageDataSource extends AbstractRemoteDataSource implements MessageContract.Remote {

    public RemoteMessageDataSource(RESTFulApiService api) {
        super(api);
    }

    @Override
    public Observable<List<Message>> systemMessages() {
        return mApi.systemMessages()
                .flatMap(new Func1<MultiPageResponse<Message>, Observable<List<Message>>>() {
                    @Override
                    public Observable<List<Message>> call(MultiPageResponse<Message> response) {
                        return Observable.just(response.getData());
                    }
                });
    }
}
